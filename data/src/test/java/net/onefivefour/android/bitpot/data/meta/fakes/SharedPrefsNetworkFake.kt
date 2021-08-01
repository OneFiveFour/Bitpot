package net.onefivefour.android.bitpot.data.meta.fakes

import net.onefivefour.android.bitpot.network.ISharedPrefsNetwork
import net.onefivefour.android.bitpot.network.model.api.ConnectionState
import net.openid.appauth.AuthState

class SharedPrefsNetworkFake: ISharedPrefsNetwork {

    private var authState = AuthState()

    override var cacheDir: String = "SharedPrefsNetwork.cacheDir"

    override var lastRepositoryFullName = "SharedPrefsNetwork.selectedRepositoryFullName"

    override var connectionState: ConnectionState = ConnectionState.UNKNOWN

    override fun getAuthState(): AuthState = authState

    override fun setAuthState(authState: AuthState) {
        this.authState = authState
    }

}
