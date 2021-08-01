package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.data.model.PipelineState as AppPipelineState
import net.onefivefour.android.bitpot.network.model.pipelines.State as NetworkPipelineState

object PipelineStateDummies {

    fun getSimpleNetworkPipelineState(): NetworkPipelineState {

        val name = "State.name"
        val stage = PipelineStageDummies.getSimplePipelineStage()
        val result = PipelineResultDummies.getSimplePipelineResult()
        val type = "state"

        return NetworkPipelineState(
            name,
            stage,
            result,
            type
        )
    }

    fun getSimpleAppPipelineState() : AppPipelineState {
        return AppPipelineState.IN_PROGRESS
    }

}
