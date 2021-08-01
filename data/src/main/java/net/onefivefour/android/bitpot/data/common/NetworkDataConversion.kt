package net.onefivefour.android.bitpot.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.data.model.ResourceStatus
import net.onefivefour.android.bitpot.network.common.ApiResponse

/**
 * Implement this class to easily convert the result from a network call
 * to an app domain model. Additionally you get [ResourceStatus] like loading, success and error
 * from the wrapping [Resource] class.
 *
 * @param FROM Model class that is received from the network call
 * @param TO Model class that is expected by the app
 *
 */
abstract class NetworkDataConversion<FROM, TO> {

    private var convertedItem: TO? = null

    private val call = object : NetworkBoundResource<TO, FROM>() {
        override fun saveCallResult(item: FROM) {
            convertedItem = getConverter().toAppModel(item).also {
                processData(it)
                cacheData(it)
            }
        }

        override fun shouldFetch(data: TO?): Boolean {
            if (data == null) return true
            if (data is List<*> && data.isNullOrEmpty()) return true
            return this@NetworkDataConversion.shouldFetch(data)
        }

        override fun reactToEmptyResponse() {
            this@NetworkDataConversion.reactToEmptyResponse()
        }

        override fun loadFromDb(): LiveData<TO> = internalLoadCachedData()
        override fun createCall(): LiveData<ApiResponse<FROM>> = getNetworkCall()
    }.asLiveData()

    abstract fun getNetworkCall(): LiveData<ApiResponse<FROM>>

    abstract fun getConverter(): NetworkDataConverter<FROM, TO>

    /**
     * optionally override this method if you want to set datatype specific caching decisions
     */
    open fun shouldFetch(appData: TO) = true

    /**
     * optionally override this method if you want to do some custom processing on the resulting data.
     */
    open fun processData(appData: TO) { /* do nothing as default */
    }

    /**
     * optionally override this method if you want to do some custom actions when you receive an empty response.
     */
    open fun reactToEmptyResponse() { /* do nothing as default */
    }

    /**
     * optionally override this method if you want to cache the data somewhere (e.g. to database, sharedPrefs, etc.)
     * If you override this method, you must also override [loadCachedData] to make use of the caching mechanism.
     */
    open fun cacheData(appData: TO) { /* do nothing as default */
    }

    /**
     * optionally override this method to load cached data (e.g. from database, sharedPrefs, etc.)
     * If you override this method, you must also override [cacheData] to make use of the caching mechanism.
     */
    open fun loadCachedData(): LiveData<TO> = AbsentLiveData.create()

    /**
     * in [get] we are always using [convertedItem] to set the resulting payload.
     * Therefore we have to set/sync whatever came from [loadCachedData] to [convertedItem] before
     * returning the cached payload to [NetworkBoundResource.loadFromDb]
     */
    private fun internalLoadCachedData(): LiveData<TO> {
        val cachedData = loadCachedData()
        if (cachedData.value != null) {
            // loadCachedData() was overridden and it returned useful cached data
            convertedItem = cachedData.value
        }
        return cachedData
    }

    fun get(): LiveData<Resource<TO>> = Transformations.map(call) { resource ->

        when (resource.resourceStatus) {
            ResourceStatus.SUCCESS -> {
                if (convertedItem == null) {
                    convertedItem = resource.data
                }
                Resource.success(convertedItem, resource.httpCode)
            }
            ResourceStatus.ERROR -> Resource.error(resource.message!!, resource.httpCode, convertedItem)
            ResourceStatus.LOADING -> Resource.loading(convertedItem)
        }
    }
}