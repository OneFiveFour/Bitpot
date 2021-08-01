package net.onefivefour.android.bitpot.network

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import net.onefivefour.android.bitpot.network.model.api.ConnectionState
import net.openid.appauth.AuthState

/**
 * KotprefModel for all SharedPreferences in the network module.
 */
class SharedPrefsNetwork(context: Context) : KotprefModel(context), ISharedPrefsNetwork {

    override val kotprefName: String = "${context.packageName}_preferences_network"

    override var cacheDir by stringPref("")
    override var connectionState by enumValuePref(ConnectionState.IS_CONNECTED)
    override var lastRepositoryFullName by stringPref("")

    private var authState by stringPref() // serialized auth state

    override fun getAuthState(): AuthState? {
        if (authState.isEmpty()) {
            return null
        }

        return AuthState.jsonDeserialize(authState)
    }

    override fun setAuthState(authState: AuthState) {
        this.authState = authState.jsonSerializeString()
    }



    // If we want to use PreferenceFragment with KotprefModel, this is the way to do it:
    // var sendByLongClick by booleanPref(true, R.string.preference_send_by_long_click_key)

}