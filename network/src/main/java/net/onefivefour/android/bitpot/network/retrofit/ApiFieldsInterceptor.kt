package net.onefivefour.android.bitpot.network.retrofit

import net.onefivefour.android.bitpot.network.apifields.ApiFieldsConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * An [Interceptor] to set the field selectors of a Bitbucket API request.
 *
 * @param apiFieldsConfig An optional [ApiFieldsConfig] to de-/select certain API fields.
 */
internal class ApiFieldsInterceptor constructor(private val apiFieldsConfig: ApiFieldsConfig) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        // get field selectors as string and ignore if empty
        val apiFieldSelectors = apiFieldsConfig.get()
        val originalRequest = chain.request()
        val urlBuilder = originalRequest.url.newBuilder()

        if (apiFieldSelectors.isNotEmpty()) {
            urlBuilder.addQueryParameter("fields", apiFieldSelectors)
        }

        val request = originalRequest.newBuilder()
            .url(urlBuilder.build())
            .build()

        return chain.proceed(request)
    }
}