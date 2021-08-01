package net.onefivefour.android.bitpot.screens.repository

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.analytics.AnalyticsDestinationChangedListener
import net.onefivefour.android.bitpot.analytics.AnalyticsEvent
import net.onefivefour.android.bitpot.analytics.EventTracker
import net.onefivefour.android.bitpot.common.SnackBarPresenter
import net.onefivefour.android.bitpot.data.model.WebHookEvent
import net.onefivefour.android.bitpot.databinding.ActivityRepositoryBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * The Activity for navigating a single Repository.
 * It holds the bottom navigation and the top sheet that the user can pull down
 * to reach the notification settings for the repository.
 *
 * This activity has its own navigation host fragment.
 */
class RepositoryActivity : AppCompatActivity(), SnackBarPresenter {
    
    private lateinit var binding: ActivityRepositoryBinding

    private val eventTracker: EventTracker by inject()

    private lateinit var navView: BottomNavigationView

    private val args: RepositoryActivityArgs by navArgs()

    private val repositoryViewModel: RepositoryViewModel by viewModel { parametersOf(args.workspaceUuid, args.repositoryUuid) }
    
    private val webHookViewModel: WebHookViewModel by viewModel { parametersOf(args.workspaceUuid, args.repositoryUuid) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding = DataBindingUtil.setContentView<ActivityRepositoryBinding>(this, R.layout.activity_repository).also { binding ->
            binding.lifecycleOwner = this
            binding.repositoryViewModel = repositoryViewModel
            binding.webHookViewModel = webHookViewModel
        }

        binding.ivNotificationStatus.setOnClickListener { toggleNotificationDrawer() }

        observeViewModels()

        setupNavController()
        
        dispatchDeepLinks()
    }

    private fun toggleNotificationDrawer() {
        val ml = binding.activityRepositoryMotionLayout
        when (ml.currentState) {
            ml.endState -> ml.transitionToStart()
            else -> ml.transitionToEnd()
        }
    }

    private fun observeViewModels() {
        repositoryViewModel.repositoryColors.observe(this, Observer { colors ->
            if (colors == null) return@Observer
            adjustStatusBarIconColor(colors.textColor)
        })

        webHookViewModel.getWebHook().observe(this, { webHook ->
            if (webHook == null) {
                deactivateNotificationSwitches()
            } else {
                updateNotificationSwitches(webHook.events)
            }
        })

        webHookViewModel.getUiEvents().observe(this, { uiEvent ->
            when (uiEvent) {
                WebHookViewModel.UiEvent.IsNotAdmin -> {
                    deactivateNotificationSwitches()
                    showNoPermissionDialog()
                    eventTracker.sendEvent(AnalyticsEvent.ForcedDeactivationOfNotifications)
                }
                is WebHookViewModel.UiEvent.Error -> {
                    deactivateNotificationSwitches()
                    eventTracker.sendEvent(AnalyticsEvent.ForcedDeactivationOfNotifications)
                    showSnackBar(uiEvent.message ?: getString(R.string.unknown_error))
                }
            }
        })
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun adjustStatusBarIconColor(textColor: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        val hsv = FloatArray(3)
        Color.colorToHSV(textColor, hsv)
        
        // TODO refactor using new methods of WindowCompat. Also see FileActivity
        window.decorView.systemUiVisibility = when (hsv[2] < 0.5f) {
            true -> View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            else -> 0
        }
    }

    private fun showNoPermissionDialog() {
        eventTracker.sendEvent(AnalyticsEvent.NoPermissionDialog)
        val dialogFragment = NoPermissionDialogFragment()
        dialogFragment.show(supportFragmentManager, NoPermissionDialogFragment.TAG)
    }

    private fun updateNotificationSwitches(events: List<WebHookEvent>) {
        binding.swPipelineUpdates.isChecked = events.contains(WebHookEvent.PIPELINE_UPDATES)
        binding.swPullRequestUpdates.isChecked = events.contains(WebHookEvent.PULL_REQUEST_UPDATES)
    }

    private fun deactivateNotificationSwitches() {
        binding.swPipelineUpdates.isChecked = false
        binding.swPullRequestUpdates.isChecked = false
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.activity_repository_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        
        navController.addOnDestinationChangedListener(AnalyticsDestinationChangedListener(this))
        navView = findViewById(R.id.activity_repository_bottom_nav)
        navView.setupWithNavController(navController)
    }

    private fun dispatchDeepLinks() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.activity_repository_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        val dispatcher = DeepLinkDispatcher(navController, args)
        dispatcher.dispatchDeepLinks()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(navView, message, Snackbar.LENGTH_SHORT)
            .setAnchorView(navView)
            .show()
    }
}
