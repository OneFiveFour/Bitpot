package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.DataNetworkConverter
import net.onefivefour.android.bitpot.data.model.MergeStrategy
import net.onefivefour.android.bitpot.data.model.PostMerge as AppPostMerge
import net.onefivefour.android.bitpot.network.model.merge.PostMerge as NetworkPostMerge

/**
 * Converts a [AppPostMerge] into a app domain [NetworkPostMerge].
 */
class MergeConverter : DataNetworkConverter<AppPostMerge, NetworkPostMerge> {

    override fun toNetworkModel(from: AppPostMerge): NetworkPostMerge {

        val commitMessage = from.commitMessage
        val closeSourceBranch = from.closeSourceBranch
        val mergeStrategy = toMergeStrategy(from.mergeStrategy)

        return NetworkPostMerge(
            commitMessage,
            closeSourceBranch,
            mergeStrategy
        )
    }

    private fun toMergeStrategy(mergeStrategy: MergeStrategy): String {
        return when (mergeStrategy) {
            MergeStrategy.MERGE_COMMIT -> "merge_commit"
            MergeStrategy.SQUASH -> "squash"
            MergeStrategy.FAST_FORWARD -> "fast_forward"
        }
    }

}