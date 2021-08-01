package net.onefivefour.android.bitpot.data.common

import android.content.Context
import androidx.work.*
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.database.WebHooksDao
import net.onefivefour.android.bitpot.data.model.converter.WebHookConverter
import net.onefivefour.android.bitpot.data.repositories.WebHooksRepository
import net.onefivefour.android.bitpot.network.model.webhooks.WebHook
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.Executors

/**
 * This Worker should be run whenever the firebase token is updated.
 * This Worker then takes care of all needed actions to update the token in all places.
 * For now this means only to update all web hook urls on the server and in the local database.
 */
class UpdateFirebaseTokenWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams), KoinComponent, Callback<WebHook> {

    companion object {

        const val KEY_NEW_TOKEN = "KEY_NEW_TOKEN"

        /**
         * Creates the [OneTimeWorkRequest] to be used for this Worker.
         *
         * @param newToken The new firebase token
         */
        fun buildRequest(newToken: String): OneTimeWorkRequest {
            val inputData = workDataOf(KEY_NEW_TOKEN to newToken)

            return OneTimeWorkRequestBuilder<UpdateFirebaseTokenWorker>()
                .setInputData(inputData)
                .build()
        }

    }

    private val webHooksDao: WebHooksDao by inject()

    private val webHooksRepository: WebHooksRepository by inject()

    private val newToken = inputData.getString(KEY_NEW_TOKEN)

    override suspend fun doWork(): Result {
        Timber.d("+++ UpdateWebHooksWorker.doWork() started")

        if (newToken == null) {
            Timber.e("+++ new Token is null -> nothing to update")
            return Result.success()
        }

        // store new token
        BitpotData.setFirebaseToken(newToken)

        // get all web hooks with the wrong firebaseToken from DB
        val uuidList = webHooksDao.getAllExcept(newToken)
        Timber.d("+++ found ${uuidList.size} entries that do not have the new token")

        // update each web hook with the new firebaseToken
        uuidList.forEach { uuid ->
            Timber.d("+++ updateFirebaseToken for $uuid")
            webHooksRepository.updateFirebaseToken(uuid, this)
        }

        return Result.success()
    }

    override fun onFailure(call: Call<WebHook>, t: Throwable) {
        Timber.d("+++ Error while updating the WebHook -> delete from DB")
        Executors.newSingleThreadExecutor().execute {
            val uuid = call.request().url.encodedPathSegments.last()
            webHooksDao.delete(uuid)
        }
    }

    override fun onResponse(call: Call<WebHook>, response: Response<WebHook>) {
        val updatedWebHook = response.body() ?: return

        Timber.d("+++ Web Hook was successfully updated -> Update DB")
        Executors.newSingleThreadExecutor().execute {
            val convertedWebHook = WebHookConverter().toAppModel(updatedWebHook)
            webHooksDao.insert(convertedWebHook)
        }
    }
}