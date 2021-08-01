package net.onefivefour.android.bitpot.analytics

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.firebase.analytics.FirebaseAnalytics
import net.onefivefour.android.bitpot.R
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A [NavController.OnDestinationChangedListener] to log navigation on fragment level.
 * Firebase Analytics can only log navigation between Activities automatically. Therefore we have to set
 * the screen names of navigated fragments manually here.
 */
class AnalyticsDestinationChangedListener(private val activity: Activity) : NavController.OnDestinationChangedListener, KoinComponent {

    private val firebaseAnalytics: FirebaseAnalytics by inject()

    private val screenNameMap = mapOf(
        R.id.fragment_auth to "Login",
        R.id.fragment_markdown to "Markdown",
        R.id.fragment_repositories to "Repositories",
        R.id.fragment_legal to "Legal",
        R.id.fragment_sources to "Sources",
        R.id.fragment_pull_requests to "PullRequests",
        R.id.fragment_pipelines to "Pipelines",
        R.id.fragment_downloads to "Downloads"
    )

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {

        val screenName = screenNameMap[destination.id]

        val bundle = bundleOf(FirebaseAnalytics.Param.SCREEN_NAME to screenName)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
