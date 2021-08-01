package net.onefivefour.android.bitpot.data.model

/**
 * Used to convert the Bitbucket API string representation of the
 * target of pipeline into a proper enum used within the app.
 *
 * A pipeline target can for example be a branch or a pull request.
 * Whatever can start a pipeline is considered a pipeline target.
 */
enum class PipelineTargetType(val typeString: String) {
    BRANCH("pipeline_ref_target"),
    PULL_REQUEST("pipeline_pullrequest_target"),
    COMMIT("pipeline_commit_target"),
    UNKNOWN("")
}