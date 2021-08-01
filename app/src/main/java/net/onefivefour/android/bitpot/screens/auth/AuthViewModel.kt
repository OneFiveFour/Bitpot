package net.onefivefour.android.bitpot.screens.auth

import android.content.Intent
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.common.Event
import net.openid.appauth.*
import timber.log.Timber

/**
 * This shared ViewModel encapsulates everything that has to do with authentication.
 * This includes the whole OAuth process as well as getting the auth status of the current user.
 */
@Suppress("WildcardImport")
class AuthViewModel : ViewModel() {

    private val authEvents = MutableLiveData<Event<AuthEvent>>()

    private lateinit var authState: AuthState

    companion object {
        const val REDIRECT_URI = "net.onefivefour.android.bitpot:/oauth2redirect"
        const val AUTH_ENDPOINT = "https://bitbucket.org/site/oauth2/authorize"
        const val TOKEN_ENDPOINT = "https://bitbucket.org/site/oauth2/access_token"
    }

    fun hasAccessToken() = BitpotData.hasAccessToken()

    fun getAuthEvents(): LiveData<Event<AuthEvent>> = authEvents

    @WorkerThread
    fun beginLogin() {
        Timber.d("++ begin Login")

        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(AUTH_ENDPOINT), // authorization endpoint
            Uri.parse(TOKEN_ENDPOINT)
        ) // token endpoint

        authState = AuthState(serviceConfig)

        val authRequest = AuthorizationRequest.Builder(
            serviceConfig, // the authorization service configuration
            BitpotData.getClientId(), // the client ID, typically pre-registered and static
            ResponseTypeValues.CODE, // the response_type value: we want a code
            Uri.parse(REDIRECT_URI) // the redirect URI to which the auth response is sent
        ).setScope("email").build()

        authEvents.postValue(Event(AuthEvent.AuthRequestGeneratedEvent(authRequest)))
    }

    @WorkerThread
    fun getAuthToken(data: Intent, authService: AuthorizationService) {
        val authResponse = AuthorizationResponse.fromIntent(data)
        val authException = AuthorizationException.fromIntent(data)

        authState.update(authResponse, authException)

        when {
            authResponse != null -> exchangeTokens(authResponse, authService)
            else -> authEvents.postValue(Event(AuthEvent.OnError(authException?.localizedMessage ?: "Error during token exchange")))
        }
    }

    @WorkerThread
    private fun exchangeTokens(authResponse: AuthorizationResponse, authService: AuthorizationService) {
        val tokenRequest = authResponse.createTokenExchangeRequest()
        val clientAuth = ClientSecretBasic(BitpotData.getClientSecret())
        authService.performTokenRequest(tokenRequest, clientAuth) { tokenResponse, authorizationException ->

            authState.update(tokenResponse, authorizationException)

            when {
                tokenResponse != null -> {
                    // exchange succeeded, store access and refresh token
                    BitpotData.setAuthState(authState)
                    authEvents.postValue(Event(AuthEvent.OnSuccessfulAuth))

                }
                else -> {
                    // authorization failed, check authorizationException for more details
                    authEvents.postValue(Event(
                        AuthEvent.OnError(authorizationException?.localizedMessage ?: "Error during token exchange"))
                    )
                }
            }
        }
    }

    /**
     * Calling this will delete all userdata from
     * all persistence layers (db, shared prefs)
     */
    fun logout() = viewModelScope.launch {
        BitpotData.logout()
    }


    /**
     * An Event regarding the Login/Authorization process
     */
    sealed class AuthEvent {

        /**
         * Fired when the OAuth authorization was successful
         */
        object OnSuccessfulAuth : AuthEvent()

        /**
         * Fired when the [AuthorizationRequest] was generated
         */
        class AuthRequestGeneratedEvent(val authRequest: AuthorizationRequest) : AuthEvent()

        /**
         * Fired when an error occurred during the OAuth process
         */
        class OnError(val message: String?) : AuthEvent()
    }
}
