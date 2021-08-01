package net.onefivefour.android.bitpot.data.common

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import net.onefivefour.android.bitpot.data.model.AppExecutors
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.network.common.ApiEmptyResponse
import net.onefivefour.android.bitpot.network.common.ApiErrorResponse
import net.onefivefour.android.bitpot.network.common.ApiResponse
import net.onefivefour.android.bitpot.network.common.ApiSuccessResponse

/**
 * A generic class that can provide a resource backed by both the SQLite database and the network.
 * You can read more about it in the [Architecture Guide](https://developer.android.com/arch).
 *
 * In this app do not use this class directly. Instead use [NetworkDataConversion].
 *
 * @param ResultType Type for the Resource data
 * @param RequestType Type for the API response
 */
internal abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.postValue(Resource.loading(null))
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData, null))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    AppExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                        AppExecutors.mainThread().execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData, response.httpCode))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    AppExecutors.diskIO().execute {
                        reactToEmptyResponse()
                    }
                    AppExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData, response.code))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.errorMessage, response.httpCode, newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    open fun reactToEmptyResponse() {}

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}