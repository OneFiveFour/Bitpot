package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pipelines.Target as PipelineTarget
import net.onefivefour.android.bitpot.network.model.refs.Target as RefTarget

object TargetDummies {

    fun getSimplePipelineTarget(): PipelineTarget {

        val commit = CommitDummies.getPipelineCommit()
        val refName = "Target.refName"
        val type = "pipeline_commit_target"
        val source = "Target.source"
        val destination = "Target.destination"

        return PipelineTarget(
            commit,
            destination,
            refName,
            source,
            type
        )
    }

    fun getSimpleRefTarget(): RefTarget {

       val  hash = StringDummies.getHash()
       val  repository = RepositoryDummies.getRefRepository()

        return RefTarget(hash, repository)
    }

}
