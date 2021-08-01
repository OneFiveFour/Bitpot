package net.onefivefour.android.bitpot.screens.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import net.onefivefour.android.bitpot.BuildConfig
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.analytics.AnalyticsDestinationChangedListener
import net.onefivefour.android.bitpot.analytics.AnalyticsEvent
import net.onefivefour.android.bitpot.analytics.EventTracker
import net.onefivefour.android.bitpot.analytics.model.ContentType
import net.onefivefour.android.bitpot.data.common.UpdateFirebaseTokenWorker
import net.onefivefour.android.bitpot.data.model.Workspace
import net.onefivefour.android.bitpot.databinding.ActivityMainBinding
import net.onefivefour.android.bitpot.databinding.NavigationHeaderBinding
import net.onefivefour.android.bitpot.screens.auth.AuthViewModel
import net.onefivefour.android.bitpot.screens.menu.WorkspaceSpinnerAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


/**
 * The main entry point of the app.
 * Its main purpose is to navigate to the first fragment which is either the [net.onefivefour.android.bitpot.screens.auth.AuthFragment]
 * or the [net.onefivefour.android.bitpot.screens.repositories.RepositoriesFragment].
 *
 * It also initializes the navigation drawer and Firebase.
 *
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding : ActivityMainBinding

    private val eventTracker: EventTracker by inject()
    private val workManager: WorkManager by inject()

    private val navItemSelectedListener = BitpotNavItemSelectedListener()
    private lateinit var navController: NavController

    private val authViewModel: AuthViewModel by viewModel()
    private val workspacesViewModel: WorkspacesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initFirebase()
        initNavigation()
        initNavigationHeader()
        initNavigationFooter()
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) return@OnCompleteListener

                // Get new Instance ID token
                val token = task.result ?: return@OnCompleteListener
                Timber.d("++++ Firebase Token: $token")

                // updates existing web hooks in database and update token stored in BitpotData
                val workRequest = UpdateFirebaseTokenWorker.buildRequest(token)
                workManager.enqueue(workRequest)
            })
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.activity_main_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        
        navController.addOnDestinationChangedListener(BitpotDestinationChangedListener())
        navController.addOnDestinationChangedListener(AnalyticsDestinationChangedListener(this))
        binding.activityMainNavigationView.setupWithNavController(navController)
        binding.activityMainNavigationView.setNavigationItemSelectedListener(navItemSelectedListener)
        binding.activityMainToolbar.setupWithNavController(navController, binding.activityMainDrawerLayout)
    }

    private fun initNavigationHeader() {
        val navigationHeader = binding.activityMainNavigationView.getHeaderView(0)
        NavigationHeaderBinding.bind(navigationHeader).apply {
            viewModel = workspacesViewModel
            lifecycleOwner = this@MainActivity
        }

        // setup workspace spinner
        val workspaceSpinner = navigationHeader.findViewById<Spinner>(R.id.sp_workspaces)
        workspaceSpinner.adapter = WorkspaceSpinnerAdapter(workspaceSpinner.context, android.R.layout.simple_spinner_dropdown_item, arrayListOf())
        workspaceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { /* do nothing */ }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // if a workspace is selected, tell the viewModel about it.
                val selectedWorkspace = workspaceSpinner.selectedItem as Workspace
                workspacesViewModel.setSelectedWorkspaceUuid(selectedWorkspace.uuid)
                binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START)

                // also log this event
                eventTracker.sendEvent(AnalyticsEvent.SelectContent(ContentType.WORKSPACE))
            }
        }

        workspacesViewModel.workspaces.observe(this, Observer { workspaces ->
            workspaces ?: return@Observer
            if (workspaces.size == 1) {
                // we have exactly 1 workspace
                // -> The workspace spinner wont trigger in this case
                // -> set this workspace as selected workspace
                workspacesViewModel.setSelectedWorkspaceUuid(workspaces.first().uuid)
            }
        })
    }

    private fun initNavigationFooter() {
        binding.tvAppVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.COMMIT_HASH)
    }

    private fun sendFeedback() {
        // log this event
        eventTracker.sendEvent(AnalyticsEvent.SelectContent(ContentType.SEND_FEEDBACK))
        Intent(Intent.ACTION_SEND).apply {
            type = "text/html"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.feedback_email_address)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_email_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_email_body))
            startActivity(Intent.createChooser(this, getString(R.string.send_feedback)))
        }
    }

    private fun logout() {
        // start logout
        val logoutJob = authViewModel.logout()

        // once logoutJob is completed...
        logoutJob.invokeOnCompletion { throwable ->

            // ...log errors if any
            if (throwable != null) {
                Timber.e("Error during Logout: ${throwable.message}")
                Timber.e(throwable)
            }

            // ...navigate to AuthActivity
            val options = NavOptions.Builder()
                .setPopUpTo(navController.currentDestination!!.id, true)
                .setLaunchSingleTop(true)
                .build()
            navController.navigate(R.id.action_global_authActivity, null, options)
            finish()
        }
    }

    /**
     * Called on every change of navigation destination.
     * Used for example to set special Toolbar titles or check for authentication state.
     */
    inner class BitpotDestinationChangedListener : NavController.OnDestinationChangedListener {
        override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
            // check login on every destination change
            if (!authViewModel.hasAccessToken() || destination.id == R.id.activity_auth) {
                logout()
            }
        }
    }

    /**
     * Called whenever an item was selected in the Navigation View.
     * We need this listener to perform a logout. Otherwise everything is handled
     * like it would be by calling navigation_view.setupWithNavController(navController)
     */
    inner class BitpotNavItemSelectedListener : NavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            // check for special case where we want to pop everything
            // from the backstack until we reach the auth activity
            return when (item.itemId) {
                R.id.activity_auth -> {
                    logout()
                    true
                }
                R.id.send_feedback -> {
                    sendFeedback()
                    true
                }
                else -> {
                    // Fallback for all other (normal) cases.
                    val handled = NavigationUI.onNavDestinationSelected(item, navController)

                    // This is usually done by the default ItemSelectedListener.
                    // But since there can only be one ItemSelectedListener set, we have to do this.
                    if (handled) binding.activityMainDrawerLayout.closeDrawer(binding.activityMainNavigationView)

                    // return the result of NavigationUI call
                    handled
                }
            }
        }
    }
}
