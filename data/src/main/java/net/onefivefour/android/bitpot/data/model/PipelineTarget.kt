package net.onefivefour.android.bitpot.data.model

/**
 * A Pipeline target. This defines by what action the pipeline was triggered.
 * See extending classes for details.
 */
sealed class PipelineTarget(val type: PipelineTargetType) {

    /**
     * It is not known by what action this pipeline was started.
     * Fallback value.
     */
    object Unknown: PipelineTarget(PipelineTargetType.UNKNOWN)

    /**
     * This pipeline was triggered by pushing a branch to Bitbucket.
     *
     * @param branchName the name of the pushed branch
     */
    class Branch(val branchName: String) : PipelineTarget(PipelineTargetType.BRANCH)

    /**
     * This pipeline was triggered by creating a pull request.
     *
     * @param source the name of source branch
     * @param destination the name of the destination branch
     */
    class PullRequest(val source: String, val destination: String) : PipelineTarget(PipelineTargetType.PULL_REQUEST)

    /**
     * This pipeline was triggered for a specific commit
     *
     * @param commitMessage the message of the commit
     */
    class Commit(val commitMessage: String): PipelineTarget(PipelineTargetType.COMMIT)
}
