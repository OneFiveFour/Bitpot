package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pipelines.Commit as PipelineCommit
import net.onefivefour.android.bitpot.network.model.sources.Commit as SourceCommit

object CommitDummies {


    fun getSourceCommit(): SourceCommit {
        val hash = StringDummies.getHash()
        return SourceCommit(hash)
    }

    fun getPipelineCommit(): PipelineCommit {
        val message = StringDummies.getRawString()
        return PipelineCommit(message)
    }
}
