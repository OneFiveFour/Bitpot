package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pipelines.Result

object PipelineResultDummies {
    fun getSimplePipelineResult(): Result {

        val name = "RUNNING"
        val type = "pipelineResult"

        return Result(
            name,
            type
        )
    }

}
