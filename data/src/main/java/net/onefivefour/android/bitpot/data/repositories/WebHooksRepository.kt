package net.onefivefour.android.bitpot.data.repositories

import androidx.lifecycle.LiveData
import net.onefivefour.android.bitpot.data.common.NetworkBoundResource
import net.onefivefour.android.bitpot.data.common.NetworkDataConversion
import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.database.WebHooksDao
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.data.model.WebHookEvent
import net.onefivefour.android.bitpot.data.model.converter.UnitConverter
import net.onefivefour.android.bitpot.data.model.converter.WebHookConverter
import net.onefivefour.android.bitpot.data.model.converter.WebHooksSyncingConverter
import net.onefivefour.android.bitpot.network.apifields.WebHookApiFields
import net.onefivefour.android.bitpot.network.apifields.WebHooksApiFields
import net.onefivefour.android.bitpot.network.common.ApiResponse
import net.onefivefour.android.bitpot.network.model.webhooks.WebHooks
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import retrofit2.Callback
import timber.log.Timber
import net.onefivefour.android.bitpot.data.model.WebHook as AppWebHook
import net.onefivefour.android.bitpot.network.model.webhooks.WebHook as NetworkWebHook

/**
 * This repository delivers all data relevant for WebHooks.
 * WebHooks are used to de-/activate push notifications for all possible [WebHookEvent]s.
 *
 * Use this Repository to create, get, update or delete the WebHook of the current Bitbucket repository.
 */
class WebHooksRepository(private val webHooksDao: WebHooksDao) {


    /**
     * @return LiveData of the WebHook for the current repository
     */
    fun getWebHook(workspaceUuid: String, repositoryUuid: String): LiveData<Resource<AppWebHook>> {
        Timber.d("+++ WebHooksRepository.getWebHook() called")
        return object : NetworkBoundResource<AppWebHook, WebHooks>() {

            override fun saveCallResult(item: WebHooks) {
                val convertedWebHook = WebHooksSyncingConverter().toAppModel(item) ?: return
                webHooksDao.insert(convertedWebHook)
            }

            override fun shouldFetch(data: AppWebHook?) = true

            override fun loadFromDb(): LiveData<AppWebHook> = webHooksDao.get(workspaceUuid, repositoryUuid)

            override fun createCall(): LiveData<ApiResponse<WebHooks>> = BitbucketService.get(WebHooksApiFields()).getWebHooks(workspaceUuid, repositoryUuid)

        }.asLiveData()
    }

    /**
     * Call this to create a new WebHook for the current repository.
     *
     * @param webHookEvents these events are activated in the new WebHook.
     * @return LiveData of the [AppWebHook] of the current repository, wrapped in a [Resource]
     */
    fun createWebHook(webHookEvents: List<WebHookEvent>): LiveData<Resource<AppWebHook>> {
        Timber.d("+++ WebHooksRepository.createWebHook() called")
        val postWebHook = WebHookConverter().toNetworkModel(webHookEvents)
        return object : NetworkDataConversion<NetworkWebHook, AppWebHook>() {
            override fun getNetworkCall(): LiveData<ApiResponse<NetworkWebHook>> = BitbucketService.get(WebHookApiFields()).postWebHook(postWebHook)
            override fun getConverter(): NetworkDataConverter<NetworkWebHook, AppWebHook> = WebHookConverter()
            override fun processData(appData: AppWebHook) = webHooksDao.insert(appData)
        }.get()
    }

    /**
     * Call this to update the given [AppWebHook]. All of the given events will be active after the update call.
     *
     * @return LiveData of the [AppWebHook] after the update, wrapped in a [Resource]
     */
    fun updateWebHook(webHook: AppWebHook, webHookEvents: List<WebHookEvent>): LiveData<Resource<AppWebHook>> {
        Timber.d("+++ WebHooksRepository.updateWebHook() called")
        val postWebHook = WebHookConverter().toNetworkModel(webHookEvents)
        return object : NetworkDataConversion<NetworkWebHook, AppWebHook>() {
            override fun getNetworkCall(): LiveData<ApiResponse<NetworkWebHook>> = BitbucketService.get(WebHookApiFields()).updateWebHook(postWebHook, webHook.uuid)
            override fun getConverter(): NetworkDataConverter<NetworkWebHook, AppWebHook> = WebHookConverter()
            override fun processData(appData: AppWebHook) = webHooksDao.insert(appData)
        }.get()
    }

    /**
     * Call this to delete the WebHook with the given Id.
     *
     * @return A [Resource] without Content. It can be used to determine the network status of the call.
     */
    fun deleteWebHook(uuid: String): LiveData<Resource<Unit>> {
        Timber.d("+++ WebHooksRepository.deleteWebHook() called")
        return object : NetworkDataConversion<Unit, Unit>() {
            override fun getNetworkCall(): LiveData<ApiResponse<Unit>> = BitbucketService.get().deleteWebHook(uuid)
            override fun getConverter(): NetworkDataConverter<Unit, Unit> = UnitConverter()
            override fun reactToEmptyResponse() = webHooksDao.delete(uuid)
        }.get()
    }

    /**
     * Call this method to update the given web hooks firebase token
     *
     * @return LiveData of the [AppWebHook] after the update, wrapped in a [Resource]
     */
    fun updateFirebaseToken(uuid: String, callback: Callback<NetworkWebHook>) {
        Timber.d("+++ WebHooksRepository.updateFirebaseToken() called")
        val appWebHook = webHooksDao.get(uuid)
        val postWebHook = WebHookConverter().toNetworkModel(appWebHook)
        BitbucketService.get(WebHookApiFields()).updateFirebaseToken(appWebHook.workspaceUuid, appWebHook.repositoryUuid, appWebHook.uuid, postWebHook).enqueue(callback)
    }

    /**
     * Call this to delete the WebHook with the given Id silently.
     */
    fun deleteWebHook(uuid: String, callback: Callback<Unit>) {
        Timber.d("+++ WebHooksRepository.deleteWebHookSilent() called")
        BitbucketService.get().deleteWebHookSilent(uuid).enqueue(callback)
    }
}