package net.onefivefour.android.bitpot.network.retrofit

import net.onefivefour.android.bitpot.network.BuildConfig
import net.onefivefour.android.bitpot.network.model.auth.RefreshTokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * The Retrofit API definition for oauth2 calls toward bitbucket.
 */
interface AuthApi {

    companion object {
        const val BASE_URL = "https://bitbucket.org/site/oauth2/"
        const val CLIENT_ID = BuildConfig.BITBUCKET_CLIENT_ID
        const val CLIENT_SECRET = BuildConfig.BITBUCKET_CLIENT_SECRET
    }

    @FormUrlEncoded
    @POST("access_token")
    fun refreshAuthToken(@Field("grant_type") grantTypeValues: String, @Field("refresh_token") refreshToken: String) : Call<RefreshTokenResponse>
}