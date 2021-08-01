package net.onefivefour.android.bitpot.screens.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.analytics.AnalyticsDestinationChangedListener
import net.onefivefour.android.bitpot.databinding.ActivityAuthBinding
import net.onefivefour.android.bitpot.screens.legal.LegalViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Activity that contains the authorization navigation graph.
 * For now it only contains the AuthFragment.
 * It is responsible for the conditional navigation between
 * [AuthFragment] and [net.onefivefour.android.bitpot.screens.main.MainActivity]
 */
class AuthActivity : AppCompatActivity() {

    private val authViewModel : AuthViewModel by viewModel()
    private val consentViewModel: ConsentViewModel by viewModel()
    private val legalViewModel: LegalViewModel by viewModel()

    private lateinit var binding: ActivityAuthBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        initConsent()
    }

    private fun initConsent() {
        consentViewModel.getCrashlyticsConsent().observe(this, { consentGiven ->
            val state = if (consentGiven) BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(binding.bottomSheet).state = state
        })

        binding.btnConsentAgree.setOnClickListener {
            consentViewModel.grantCrashlyticsConsent()
            consentViewModel.setAnalyticsConsent(binding.swAnalytics.isChecked)
            consentViewModel.setPersonalizedAdsConsent(binding.swPersonalizedAds.isChecked)
        }
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.activity_auth_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        
        if (authViewModel.hasAccessToken()) {
            navController.navigate(R.id.activity_main)
            finish()
        } else {
            navController.addOnDestinationChangedListener(BitpotDestinationChangedListener())
            navController.addOnDestinationChangedListener(AnalyticsDestinationChangedListener(this))
            binding.activityAuthToolbar.setupWithNavController(navController)
        }

        binding.btnPrivacyPolicy.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
            Handler(Looper.getMainLooper()).postDelayed( {
                val title = getString(legalViewModel.privacy.titleRes)
                val fileName = legalViewModel.privacy.fileName
                val action = AuthFragmentDirections.actionFragmentAuthToFragmentMarkdownLoggedOut(fileName, title)
                navController.navigate(action)
            }, 200)

        }
    }

    /**
     * Called on every change of navigation destination.
     * Used for example to set special Toolbar titles or check for authentication state.
     */
    inner class BitpotDestinationChangedListener : NavController.OnDestinationChangedListener {
        override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
            binding.activityAuthToolbar.title = ""
            if (destination.id == R.id.fragment_auth && !consentViewModel.isCrashlyticsConsentGiven()) {
                BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}