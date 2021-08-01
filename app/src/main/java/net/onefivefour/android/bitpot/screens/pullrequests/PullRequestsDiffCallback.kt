package net.onefivefour.android.bitpot.screens.pullrequests

import androidx.recyclerview.widget.DiffUtil
import net.onefivefour.android.bitpot.data.model.PullRequest

/**
 * This callback is used to calculate the difference of to lists of [PullRequest]s.
 * Be as specific as possible when comparing the contents to avoid strange behavior.
 */
class PullRequestsDiffCallback : DiffUtil.ItemCallback<PullRequest>() {
    
    override fun areItemsTheSame(oldItem: PullRequest, newItem: PullRequest): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PullRequest, newItem: PullRequest): Boolean {
        return arePullRequestsTheSame(oldItem, newItem)    
    }

    private fun arePullRequestsTheSame(oldItem: PullRequest, newItem: PullRequest): Boolean {
        return oldItem.destinationBranchName == newItem.destinationBranchName &&
                oldItem.sourceBranchName == newItem.sourceBranchName &&
                oldItem.lastUpdated == newItem.lastUpdated
    }
}