package net.onefivefour.android.bitpot.screens.repository

import android.os.Bundle
import androidx.navigation.NavController
import net.onefivefour.android.bitpot.R

/**
 * A utility class to find the correct navigation target depending on the incoming deep link params.
 */
class DeepLinkDispatcher(private val navController: NavController, private val args: RepositoryActivityArgs) {


    /**
     * Selects the bottom navigation item in [RepositoryActivity] depending on the [args.targetView] value.
     */
    fun dispatchDeepLinks() {
        // set start destination from incoming (deep link) argument
        val navGraph = navController.graph
        navGraph.startDestination = when (args.targetView) {
            "pipelines" -> R.id.fragment_pipelines
            "pull_requests" -> R.id.fragment_pull_requests
            "downloads" -> R.id.fragment_downloads
            "sources" -> R.id.fragment_sources
            else -> R.id.fragment_sources
        }
        navController.graph = navGraph


        if (args.targetId.isNotBlank()) {
            val arguments = Bundle().apply {
                putString("workspaceUuid", args.workspaceUuid)
                putString("repositoryUuid", args.repositoryUuid)
                putInt("pullRequestId", args.targetId.toInt())
            }


            navController.navigate(R.id.action_navigation_pull_requests_to_pullRequestActivity, arguments)
        }
    }
    
}
