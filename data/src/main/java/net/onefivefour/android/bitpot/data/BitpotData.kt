package net.onefivefour.android.bitpot.data

import android.content.Context
import kotlinx.coroutines.withContext
import net.onefivefour.android.bitpot.data.common.DispatcherProvider
import net.onefivefour.android.bitpot.data.common.FileHelper
import net.onefivefour.android.bitpot.data.database.BitpotDatabase
import net.onefivefour.android.bitpot.data.database.DownloadListener
import net.onefivefour.android.bitpot.data.model.ConnectionState
import net.onefivefour.android.bitpot.network.BitpotNetwork
import net.onefivefour.android.bitpot.network.ISharedPrefsNetwork
import net.onefivefour.android.bitpot.network.retrofit.AuthApi
import net.openid.appauth.AuthState
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.io.File
import net.onefivefour.android.bitpot.network.model.api.ConnectionState as NetworkConnectionState

/**
 * This singleton provides some convenience methods for the UI layer.
 * Mostly to access Shared preferences stored in the data- or in the network-module.
 */
object BitpotData : KoinComponent {

    private val sharedPrefsNetwork : ISharedPrefsNetwork by inject()
    private val sharedPrefsData: ISharedPrefsData by inject()
    private val database: BitpotDatabase by inject()
    private val dispatcherProvider: DispatcherProvider by inject()

    fun init(context: Context) {
        BitpotNetwork.init(context)
        FileHelper.setDownloadPath(context)
    }

    fun setAuthState(authState: AuthState) {
        sharedPrefsNetwork.setAuthState(authState)
    }

    fun hasAccessToken() = !sharedPrefsNetwork.getAuthState()?.accessToken.isNullOrEmpty()

    suspend fun logout() {
        Timber.d("+ Logout: Delete all user data")
        sharedPrefsNetwork.setAuthState(AuthState())
        sharedPrefsData.selectedWorkspaceUuid = null
        sharedPrefsData.accountId = ""
        withContext(dispatcherProvider.io()) {
            database.clearAllTables()
        }
    }

    fun getClientId() = AuthApi.CLIENT_ID

    fun getClientSecret() = AuthApi.CLIENT_SECRET

    fun setCacheDir(cacheDir: File) { sharedPrefsNetwork.cacheDir = cacheDir.path }

    fun setConnectionState(connectionState: ConnectionState) {

        // since the app module knows nothing about the network module,
        // but the connection state can only be determined by the app module
        // we have to do this ugly mapping from one enum in data to the same enum in network.
        val mappedState = when (connectionState) {
            ConnectionState.IS_CONNECTED -> NetworkConnectionState.IS_CONNECTED
            ConnectionState.IS_DISCONNECTED -> NetworkConnectionState.IS_DISCONNECTED
        }

        sharedPrefsNetwork.connectionState = mappedState
    }

    fun getSelectedWorkspaceUuid() = sharedPrefsData.selectedWorkspaceUuid

    fun getSelectedWorkspaceUuidLiveData() = sharedPrefsData.getSelectedWorkspaceUuidLiveData()

    fun setSelectedWorkspaceUuid(workspaceUuid: String) {
        sharedPrefsData.selectedWorkspaceUuid = workspaceUuid
    }

    fun download(downloadId: String, url: String) {
        val destinationFile = FileHelper.getDownloadDestination(url)
        BitpotNetwork.downloadFile(downloadId, url, destinationFile, DownloadListener())
    }

    fun getFirebaseToken() = sharedPrefsData.firebaseToken

    fun setFirebaseToken(token: String) { sharedPrefsData.firebaseToken = token }

    fun getAccountId(): String = sharedPrefsData.accountId
    
    fun setLastRepositoryFullName(repoFullName: String) {
        Timber.d("+++ setLastRepositoryFullName to $repoFullName")
        sharedPrefsNetwork.lastRepositoryFullName = repoFullName
    }
}