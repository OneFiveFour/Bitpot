package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.extensions.toInstant
import net.onefivefour.android.bitpot.data.model.Pipeline
import net.onefivefour.android.bitpot.data.model.PipelineState
import net.onefivefour.android.bitpot.data.model.PipelineTarget
import net.onefivefour.android.bitpot.data.model.PipelineTargetType
import net.onefivefour.android.bitpot.network.model.pipelines.State
import net.onefivefour.android.bitpot.network.model.pipelines.Target
import timber.log.Timber
import net.onefivefour.android.bitpot.network.model.pipelines.Pipeline as NetworkPipeline

/**
 * Converts a [NetworkPipeline] into a app domain [Pipeline].
 */
class PipelineConverter : NetworkDataConverter<NetworkPipeline, Pipeline> {

    /**
     * use this map for reverse lookups to get a PipelineTargetType by its api typeString.
     * Usage: 'pipelineTargets[<typeString>]'
     */
    private val pipelineTargets = PipelineTargetType.values().associateBy(PipelineTargetType::typeString)

    override fun toAppModel(item: NetworkPipeline): Pipeline {
        return Pipeline(
            item.uuid,
            item.repository.uuid,
            item.buildNumber,
            toPipelineTarget(item.target),
            toPipelineState(item.state),
            item.createdOn.toInstant()
        )
    }

    private fun toPipelineTarget(target: Target): PipelineTarget {
        return when (pipelineTargets[target.type]) {
            PipelineTargetType.BRANCH -> PipelineTarget.Branch(target.refName)
            PipelineTargetType.PULL_REQUEST -> PipelineTarget.PullRequest(target.source, target.destination)
            PipelineTargetType.COMMIT -> PipelineTarget.Commit(target.commit.message)
            PipelineTargetType.UNKNOWN,
            null -> PipelineTarget.Unknown
        }
    }

    /**
     * Uses the state and result of the given [State] to return a
     * [PipelineState] used within the app.
     */
    @Suppress("ComplexMethod")
    private fun toPipelineState(state: State): PipelineState {

        val stateName = state.name
        val stateResult = state.result?.name
        val stateStage = state.stage?.name

        if (stateName == "PENDING") {
            return when (stateResult) {
                null,
                "PENDING" -> PipelineState.PENDING
                else -> logUnknownPipelineState(stateName, stateResult, stateStage)
            }
        }

        if (stateName == "IN_PROGRESS") {
            when (stateResult) {
                "RUNNING" -> return PipelineState.IN_PROGRESS
            }
            when (stateStage) {
                "PAUSED" -> return PipelineState.PAUSED
                "RUNNING" -> return PipelineState.IN_PROGRESS
            }
            logUnknownPipelineState(stateName, stateResult, stateStage)
        }

        if (stateName == "COMPLETED") {
            return when (stateResult) {
                "SUCCESSFUL" -> PipelineState.SUCCESSFUL
                "STOPPED" -> PipelineState.STOPPED
                "EXPIRED" -> PipelineState.EXPIRED
                "FAILED" -> PipelineState.FAILED
                "ERROR" -> PipelineState.FAILED
                else -> logUnknownPipelineState(stateName, stateResult, stateStage)
            }
        }

        return logUnknownPipelineState(stateName, stateResult, stateStage)
    }

    private fun logUnknownPipelineState(state: String, result: String?, stage: String?): PipelineState {
        Timber.e("+++ Got unknown pipeline state/result/stage combination: $state/$result/$stage")
        return PipelineState.UNKNOWN
    }
}
