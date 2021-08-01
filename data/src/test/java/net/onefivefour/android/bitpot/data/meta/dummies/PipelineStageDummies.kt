package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pipelines.Stage

object PipelineStageDummies {
    fun getSimplePipelineStage(): Stage {

        val name = "IN_PROGRESS"
        val type = "pipelineStage"

        return Stage(
            name,
            type
        )
    }

}
