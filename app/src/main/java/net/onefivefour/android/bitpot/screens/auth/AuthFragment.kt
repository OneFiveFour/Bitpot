package net.onefivefour.android.bitpot.screens.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.snackbar.Snackbar
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.analytics.AnalyticsEvent
import net.onefivefour.android.bitpot.analytics.EventTracker
import net.onefivefour.android.bitpot.analytics.model.LoginOutcome
import net.onefivefour.android.bitpot.data.common.Event
import net.onefivefour.android.bitpot.databinding.FragmentAuthBinding
import net.onefivefour.android.bitpot.screens.legal.LegalViewModel
import net.onefivefour.android.bitpot.screens.main.UserViewModel
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * This is the fragment to Login via OAuth. This fragment is closely bound to the [AuthViewModel]
 * If Login was successful, the user is navigated to the [net.onefivefour.android.bitpot.screens.repositories.RepositoriesFragment].
 * Whenever this Fragment is created, the user gets logged out and all his/her credentials are being deleted.
 */
class AuthFragment : Fragment() {

    private val eventTracker: EventTracker by inject()

    private val authService: AuthorizationService by inject()
    private val authViewModel: AuthViewModel by viewModel()
    private val consentViewModel: ConsentViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()
    private val legalViewModel: LegalViewModel by viewModel()

    private lateinit var binding: FragmentAuthBinding

    companion object {
        const val REQUEST_CODE_AUTH = 1504
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false).also {
            // set lifecycle to make correct use of data binding
            it.lifecycleOwner = this

            // tell the binding what viewModel to use
            it.consentViewModel = consentViewModel
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLoginButton()
        setupImprintAndPrivacy()
        authViewModel.getAuthEvents().observe(viewLifecycleOwner, { handleLoginEvent(it) })
    }

    private fun setupImprintAndPrivacy() {
        binding.tvImprint.setOnClickListener {
            val title = getString(legalViewModel.imprint.titleRes)
            val fileName = legalViewModel.imprint.fileName
            val action = AuthFragmentDirections.actionFragmentAuthToFragmentMarkdownLoggedOut(fileName, title)
            findNavController().navigate(action)
        }
        binding.tvPrivacy.setOnClickListener {
            val title = getString(legalViewModel.privacy.titleRes)
            val fileName = legalViewModel.privacy.fileName
            val action = AuthFragmentDirections.actionFragmentAuthToFragmentMarkdownLoggedOut(fileName, title)
            findNavController().navigate(action)
        }
    }

    /**
     * sets up the loading animation of the Login button
     * Also its click listener.
     */
    private fun setupLoginButton() {
        bindProgressButton(binding.btnLogin)
        binding.btnLogin.attachTextChangeAnimator()
        binding.btnLogin.setOnClickListener { doLogin() }
    }

    private fun doLogin() {
        binding.btnLogin.showProgress {
            buttonTextRes = R.string.login
            progressColor = Color.WHITE
        }
        Handler(Looper.getMainLooper()).postDelayed({ authViewModel.beginLogin() }, 500)
    }

    private fun handleLoginEvent(event: Event<AuthViewModel.AuthEvent>) {
        // check if event was already handled
        if (event.wasConsumedBy(this)) {
            return
        }

        // let UI react to event
        when (val eventData = event.content) {
            is AuthViewModel.AuthEvent.AuthRequestGeneratedEvent -> doAuthorization(eventData.authRequest)
            AuthViewModel.AuthEvent.OnSuccessfulAuth -> onLoginSuccess()
            is AuthViewModel.AuthEvent.OnError -> onLoginError(eventData.message.toString())
        }

        // prevent event from being handled again
        event.consume(this)
    }

    private fun doAuthorization(authRequest: AuthorizationRequest) {
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        // TODO refactor this following https://forums.bignerdranch.com/t/solution-for-startactivityforresult-deprecated-in-fragment-1-3-0/18664
        startActivityForResult(authIntent, REQUEST_CODE_AUTH)
    }

    private fun onLoginSuccess() {
        // once we are logged in, we have to fetch the account id and...
        userViewModel.getAccountId().observe(viewLifecycleOwner, Observer { accountId ->
            if (accountId.isNullOrEmpty()) return@Observer

            eventTracker.sendEvent(AnalyticsEvent.Login(LoginOutcome.SUCCESS))
            binding.btnLogin.hideProgress(R.string.success)
            Handler(Looper.getMainLooper()).postDelayed({ navigateToApp() }, 1000)
        })
    }

    private fun navigateToApp() {
        findNavController().navigate(R.id.action_authFragment_to_mainActivity)
        requireActivity().finish()
    }

    private fun onLoginError(errorMessage: String) {
        eventTracker.sendEvent(AnalyticsEvent.Login(LoginOutcome.ERROR))
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
        binding.btnLogin.hideProgress(R.string.login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_AUTH) {
            if (data == null) return
            authViewModel.getAuthToken(data, authService)
        }
    }
}
