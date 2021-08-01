package net.onefivefour.android.bitpot.network.retrofit

import android.text.TextUtils
import net.onefivefour.android.bitpot.network.BuildConfig
import net.onefivefour.android.bitpot.network.ISharedPrefsNetwork
import net.onefivefour.android.bitpot.network.apifields.AllApiFields
import net.onefivefour.android.bitpot.network.apifields.ApiFieldsConfig
import net.onefivefour.android.bitpot.network.common.LiveDataCallAdapterFactory
import net.onefivefour.android.bitpot.network.model.api.ConnectionState
import net.onefivefour.android.bitpot.network.retrofit.AuthService.HEADER_AUTHORIZATION
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService.tokenAuthenticator
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.TokenResponse
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * This object is used to make API calls to Bitbucket.
 * If the access token is invalid, the authenticator in [AuthService] comes into play
 * to refresh the invalid access token: *
 * The [tokenAuthenticator] is set as authenticator to intercept any 401 response.
 * After the refresh token was used to get a new access token, the original request is sent again.
 */
object BitbucketService : KoinComponent {

    private val sharedPrefsNetwork: ISharedPrefsNetwork by inject()

    private const val REQUEST_TIMEOUT = 10


    /**
     * @param apiFieldsConfig An optional [ApiFieldsConfig] to de-/select certain API fields. Default is [AllApiFields]
     * @param getRawStringResponse Set to true to receive the API response as a String instead of a deserialized object. Default is false
     *
     * @return A generated implementation of the [BitbucketApi]
     */
    fun get(apiFieldsConfig: ApiFieldsConfig = AllApiFields(), getRawStringResponse: Boolean = false): BitbucketApi {
        val builder = Retrofit.Builder()
            .baseUrl(BitbucketApi.BASE_URL)
            .client(getApiHttpClient(apiFieldsConfig))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())

        if (getRawStringResponse) {
            builder.addConverterFactory(ScalarsConverterFactory.create())
        } else {
            builder.addConverterFactory(GsonConverterFactory.create())
        }

        return builder.build().create(BitbucketApi::class.java)
    }

    private fun getApiHttpClient(apiFieldsConfig: ApiFieldsConfig): OkHttpClient {

        val cacheSize = 10 * 1024 * 1024L // 10 MB
        val cacheDir = File(sharedPrefsNetwork.cacheDir)
        val cache = Cache(cacheDir, cacheSize)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = when (BuildConfig.DEBUG) {
            true -> HttpLoggingInterceptor.Level.BASIC
            else -> HttpLoggingInterceptor.Level.NONE
        }

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(PathParamInterceptor("repositoryFullName", sharedPrefsNetwork.lastRepositoryFullName))
            .addInterceptor(ApiFieldsInterceptor(apiFieldsConfig))
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .cache(cache)
            .authenticator(tokenAuthenticator)

        return httpClient.build()
    }

    private val headerInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")

        // Adding Authorization token (API Key)
        // Requests will be denied without API Key
        val authState = sharedPrefsNetwork.getAuthState()
        val accessToken = authState?.accessToken
        if (!TextUtils.isEmpty(accessToken)) {
            Timber.d("+ OkHttp access Token = $accessToken")
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val onlineInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 10 // read from cache for 10 seconds even if there is internet connection
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }

    private val offlineInterceptor = Interceptor { chain ->
        var request = chain.request()
        if (sharedPrefsNetwork.connectionState == ConnectionState.IS_DISCONNECTED) {
            val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
        chain.proceed(request)
    }

    /**
     * This Authenticator is used in the [BitbucketService] to intercept 401 responses.
     * If it is triggered, it uses the refresh token to get a new access token.
     * If this succeeds, the original API call is repeated with the new access token.
     */
    private val tokenAuthenticator = object : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {

            // get the refresh token
            val authState = sharedPrefsNetwork.getAuthState()
            val refreshToken = authState?.refreshToken ?: return null

            // exchange the refresh token for a new access token
            val newAuthTokenResponse = AuthService.get().refreshAuthToken(GrantTypeValues.REFRESH_TOKEN, refreshToken).execute()

            // create a TokenResponse object to update the stored auth state
            val refreshTokenResponse = newAuthTokenResponse.body() ?: return null
            val tokenResponse = with(refreshTokenResponse) {
                TokenResponse
                    .Builder(authState.createTokenRefreshRequest())
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setAccessTokenExpiresIn(expiresIn)
                    .setScopes(scopes.split(" "))
                    .setTokenType(tokenType)
                    .build()
            }

            // update the auth state and save the new access token for future requests
            authState.update(tokenResponse, null)
            sharedPrefsNetwork.setAuthState(authState)

            // Add new header to rejected request and retry it
            return response.request.newBuilder()
                .header(HEADER_AUTHORIZATION, "Bearer ${refreshTokenResponse.accessToken}")
                .build()
        }
    }
}