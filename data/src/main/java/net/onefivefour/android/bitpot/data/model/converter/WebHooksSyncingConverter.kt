package net.onefivefour.android.bitpot.data.model.converter

import androidx.work.WorkManager
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.common.DeleteWebHooksWorker
import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.common.UpdateFirebaseTokenWorker
import net.onefivefour.android.bitpot.data.database.WebHooksDao
import org.koin.core.KoinComponent
import org.koin.core.inject
import net.onefivefour.android.bitpot.data.model.WebHook as AppWebHook
import net.onefivefour.android.bitpot.network.model.webhooks.WebHooks as NetworkWebHooks

/**
 * Converts a [NetworkWebHooks] into a app domain [AppWebHook].
 *
 * Since the state of the server and the local database should always be the same, this converter has also
 * the responsibility to do this. So it takes in the whole list of web hooks stored on the server-side and
 * depending on the content of this list decides how to sync database with server.
 *
 * In general there are 4 possible constructs:
 *
 * 1. No WebHook from this repository
 * 2. Only WebHooks with valid firebase tokens
 * 3. Only WebHooks with invalid firebase tokens
 * 4. WebHooks with both valid and invalid firebase tokens
 *
 * See the method comments for details on how these options are handled. *
 */
class WebHooksSyncingConverter : NetworkDataConverter<NetworkWebHooks, AppWebHook?>, KoinComponent {

    private val firebaseToken = BitpotData.getFirebaseToken()

    private val workManager: WorkManager by inject()

    private val webHooksDao: WebHooksDao by inject()


    /**
     * Converts the given [NetworkWebHooks] into a [AppWebHook]
     *
     * By doing so, also takes care of syncing server and local database.
     */
    override fun toAppModel(item: NetworkWebHooks): AppWebHook? {

        // First filter for web hooks that belong to bitpot.
        val webHooksOfBitpot = item.values.filter {
            val hasAccountId = it.url.contains(BitpotData.getAccountId())
            val hasBitpotUrl = it.url.startsWith("https://bitpot-app.com")
            hasBitpotUrl && hasAccountId
        }

        val converter = WebHookConverter()
        val appWebHooks = webHooksOfBitpot.map { converter.toAppModel(it) }

        // (case 1 of class description) There are no web hooks for this repository on the server
        if (webHooksOfBitpot.isEmpty()) return null

        // there are web hooks for this repository on the server
        // -> separate them into those with valid firebase tokens and those with invalid firebase tokens.
        val withInvalidTokens = getInvalidTokens(appWebHooks)
        val withValidTokens = getValidTokens(appWebHooks)

        // handle case 2, 3 and 4 of class description
        // Because of the isEmpty() check before, it is not possible that both lists are empty now.
        return when {
            withValidTokens.isEmpty() -> caseOnlyInvalidTokens(withInvalidTokens)
            withInvalidTokens.isEmpty() -> caseOnlyValidTokens(withValidTokens)
            else -> caseValidAndInvalidTokens(withValidTokens, withInvalidTokens)
        }
    }

    /**
     * @return a list of all web hooks that have a valid firebase token. Sorted descending by their creation date
     */
    private fun getValidTokens(webHooksOfRepository: List<AppWebHook>) =
        webHooksOfRepository.filter { it.firebaseToken == firebaseToken }.sortedByDescending { it.createdAt }

    /**
     * @return a list of all web hooks that have an invalid firebase token. Sorted descending by their creation date
     */
    private fun getInvalidTokens(webHooksOfRepository: List<AppWebHook>) =
        webHooksOfRepository.filter { it.firebaseToken != firebaseToken }.sortedByDescending { it.createdAt }.toMutableList()



    /**
     * This is case 2 of the class description:
     *
     * On the server there are only web hooks with valid firebase tokens. At least 1 of them, maybe more.
     * -> Pick the newest valid web hook as result. Delete the others.
     */
    private fun caseOnlyValidTokens(withInvalidTokens: List<AppWebHook>): AppWebHook {
        // pick the newest web hook
        val resultWebHook = withInvalidTokens.first()

        // clear database for this repository
        webHooksDao.delete(resultWebHook.workspaceUuid, resultWebHook.repositoryUuid)

        // return the newest web hook
        return resultWebHook
    }

    /**
     * This is case 3 of the class description:
     *
     * On the server there are only web hooks with invalid firebase tokens. At least 1 of them, maybe more.
     * -> Pick the newest invalid web hook as result. Put it into the database and update its firebase token. Delete the others.
     */
    private fun caseOnlyInvalidTokens(withInvalidTokens: List<AppWebHook>): AppWebHook {
        // clear database for this repository
        val resultWebHook = withInvalidTokens.first()
        webHooksDao.delete(resultWebHook.workspaceUuid, resultWebHook.repositoryUuid)

        // Put the first invalid web hook into the database and let UpdateFirebaseTokenWorker do the rest
        webHooksDao.insert(resultWebHook)
        val workRequest = UpdateFirebaseTokenWorker.buildRequest(firebaseToken)
        workManager.enqueue(workRequest)

        // delete all other tokens
        val uuidsToDelete = withInvalidTokens.drop(1).map { it.uuid }
        val deleteWorkRequest = DeleteWebHooksWorker.buildRequest(uuidsToDelete)
        workManager.enqueue(deleteWorkRequest)

        return resultWebHook
    }

    /**
     * This is case 4 of the class description:
     *
     * On the server there are web hooks with both valid and invalid tokens. At least 1 of each, maybe more.
     * -> Pick the newest web hook with a valid firebase token as a result. Delete the others.
     */
    private fun caseValidAndInvalidTokens(withValidTokens: List<AppWebHook>, withInvalidTokens: List<AppWebHook>): AppWebHook {

        // pick first valid token
        val resultWebHook = withValidTokens.first()

        // clear database for this repository
        webHooksDao.delete(resultWebHook.workspaceUuid, resultWebHook.repositoryUuid)

        // create uuids to delete
        val uuidsToDelete = (withInvalidTokens + withValidTokens.drop(1)).map { it.uuid }
        val deleteWorkRequest = DeleteWebHooksWorker.buildRequest(uuidsToDelete)
        workManager.enqueue(deleteWorkRequest)

        return resultWebHook
    }
}
