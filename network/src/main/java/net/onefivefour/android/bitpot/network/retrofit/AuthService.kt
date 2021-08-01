package net.onefivefour.android.bitpot.network.retrofit

import net.onefivefour.android.bitpot.network.BuildConfig
import net.onefivefour.android.bitpot.network.utils.encodeBase64ToString
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * This object is used to refresh the access token whenever it is expired.
 */
object AuthService : KoinComponent {

    private const val REQUEST_TIMEOUT = 10
    const val HEADER_AUTHORIZATION = "Authorization"

    /**
     * @return A generated implementation of the [AuthApi]
     */
    fun get(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(AuthApi.BASE_URL)
            .client(getAuthHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    private fun getAuthHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor(headerInterceptor)

        return httpClient.build()
    }

    private fun getLoggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = when (BuildConfig.DEBUG) {
            true -> HttpLoggingInterceptor.Level.BASIC
            else -> HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }

    /**
     * This interceptor configures the headers for the refresh_token call.
     * To get a new access token, this call must provide a Base64 encoded authorization String
     * made of the client ID and the client secret.
     */
    private val headerInterceptor = Interceptor { chain ->
        val original = chain.request()
        val authentication = "${AuthApi.CLIENT_ID}:${AuthApi.CLIENT_SECRET}".toByteArray().encodeBase64ToString()

        val requestBuilder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader(HEADER_AUTHORIZATION, "Basic $authentication")

        val request = requestBuilder.build()
        chain.proceed(request)
    }


}
