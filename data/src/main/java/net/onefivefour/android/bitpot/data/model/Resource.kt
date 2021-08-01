package net.onefivefour.android.bitpot.data.model

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

/**
 * A generic class that holds a value with its [ResourceStatus].
 *
 * @param T the model class to hold here
 */
data class Resource<out T>(val resourceStatus: ResourceStatus, val httpCode: Int?, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?, successCode: Int?): Resource<T> = Resource(ResourceStatus.SUCCESS, successCode, data, null)
        fun <T> error(message: String?, errorCode: Int?, data: T?): Resource<T> = Resource(ResourceStatus.ERROR, errorCode, data, message)
        fun <T> loading(data: T?): Resource<T> = Resource(ResourceStatus.LOADING, null, data, null)
    }
}