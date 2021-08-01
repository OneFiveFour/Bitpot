package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.data.model.MergeStrategy
import net.onefivefour.android.bitpot.data.model.PostMerge

object PostMergeDummies {

    fun getPostMerge() : PostMerge {

        val commitMessage = StringDummies.getSimpleString()
        val closeSourceBranch = true
        val mergeStrategy = MergeStrategy.MERGE_COMMIT

        return PostMerge(
            commitMessage,
            closeSourceBranch,
            mergeStrategy
        )
    }
}