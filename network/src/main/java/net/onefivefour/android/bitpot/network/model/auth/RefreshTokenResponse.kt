package net.onefivefour.android.bitpot.network.model.auth

import com.google.gson.annotations.SerializedName

/**
 * This data class represents the response when exchanging a refresh token
 * for a new access token. It is used in [net.onefivefour.android.bitpot.network.retrofit.AuthService] to persist the new tokens
 * for future API calls.
 */
data class RefreshTokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("scopes")
    val scopes: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    @SerializedName("token_type")
    val tokenType: String
)