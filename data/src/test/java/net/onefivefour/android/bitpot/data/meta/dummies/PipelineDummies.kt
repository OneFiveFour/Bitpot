package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pipelines.Pipeline

object PipelineDummies {
    fun getSimplePipeline(): Pipeline {

        val buildNumber = 1
        val createdOn = StringDummies.getDateTimeString()
        val repository = RepositoryDummies.getPipelinesRepository()
        val pipelineState = PipelineStateDummies.getSimpleNetworkPipelineState()
        val target = TargetDummies.getSimplePipelineTarget()
        val uuid = "Pipeline.uuid"

        return Pipeline(
            buildNumber,
            createdOn,
            repository,
            pipelineState,
            target,
            uuid
        )
    }

}
