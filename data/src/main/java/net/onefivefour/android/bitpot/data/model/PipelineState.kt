package net.onefivefour.android.bitpot.data.model

/**
 * This data class describes the status and the result of a [Pipeline]
 */
enum class PipelineState {
    UNKNOWN,
    PENDING,
    PAUSED,
    IN_PROGRESS,
    STOPPED,
    SUCCESSFUL,
    FAILED,
    EXPIRED,
    ERROR
}