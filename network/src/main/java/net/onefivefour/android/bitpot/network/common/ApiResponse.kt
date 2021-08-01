package net.onefivefour.android.bitpot.network.common

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import net.onefivefour.android.bitpot.network.model.api.ApiError
import retrofit2.Response

/**
 * Common class used for API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message, -1)
        }

        fun <T> create(response: Response<T>, code: Int): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || code == 204) {
                    ApiEmptyResponse(code)
                } else {
                    ApiSuccessResponse(body, response.headers().toMap(), code)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = when {
                    msg.isNullOrEmpty() -> response.message()
                    else -> try {
                        Gson().fromJson(msg, ApiError::class.java).error.message
                    } catch (exception: JsonSyntaxException) {
                        msg
                    }
                }
                ApiErrorResponse(errorMsg, response.code())
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T>(val code: Int) : ApiResponse<T>()

/**
 * Represents a successful [ApiResponse]. Contains a body and the response headers.
 */
data class ApiSuccessResponse<T>(val body: T, val headers: Map<String, String>, val httpCode: Int) : ApiResponse<T>()

/**
 * Represents an error [ApiResponse]. Contains the error message and the HTTP response code.
 */
data class ApiErrorResponse<T>(val errorMessage: String?, val httpCode: Int) : ApiResponse<T>()