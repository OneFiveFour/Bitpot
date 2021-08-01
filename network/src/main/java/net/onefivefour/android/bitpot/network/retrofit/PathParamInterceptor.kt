package net.onefivefour.android.bitpot.network.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * An [Interceptor] to replace path parameters in a Retrofit BitbucketApi definition.
 *
 * @param key the string to search for within the url path.
 * @param value the value that the given key should be replaced with.
 */
internal class PathParamInterceptor constructor(key: String, private val value: String) : Interceptor {

    private val key: String = String.format("{%s}", key)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val urlBuilder = originalRequest.url.newBuilder()
        val segments = originalRequest.url.pathSegments

        segments.indices
            .filter { key.equals(segments[it], ignoreCase = true) }
            .forEach { urlBuilder.setPathSegment(it, value) }

        val request = originalRequest.newBuilder()
            .url(urlBuilder.build())
            .build()

        return chain.proceed(request)
    }
}