package net.onefivefour.android.bitpot.network

import net.onefivefour.android.bitpot.network.model.api.ConnectionState
import net.openid.appauth.AuthState


/**
 * public Api for SharedPreferences within the network context.
 */
interface ISharedPrefsNetwork {
    var cacheDir: String
    var connectionState: ConnectionState

    /**
     * The fullName field of the repository that was last fetched
     */
    var lastRepositoryFullName: String

    fun getAuthState(): AuthState?
    fun setAuthState(authState: AuthState)
}
