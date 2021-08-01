package net.onefivefour.android.bitpot.data.database.converter

import net.onefivefour.android.bitpot.data.model.PipelineTarget
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PipelineTargetConverterTest {

    private lateinit var sut: PipelineTargetConverter

    // Fake Targets
    private val unknownTarget = PipelineTarget.Unknown
    private val branchTarget = PipelineTarget.Branch("branchName")
    private val pullRequestTarget = PipelineTarget.PullRequest("source", "destination")
    private val commitTarget = PipelineTarget.Commit("source")

    // Fake serialized Targets
    private val unknownTargetString = "${PipelineTarget.Unknown.type}${PipelineTargetConverter.DELIMITER}"
    private val branchTargetString = "${branchTarget.type}${PipelineTargetConverter.DELIMITER}${branchTarget.branchName}"
    private val pullRequestTargetString = "${pullRequestTarget.type}${PipelineTargetConverter.DELIMITER}${pullRequestTarget.source}${PipelineTargetConverter.DELIMITER}${pullRequestTarget.destination}"
    private val commitTargetString = "${commitTarget.type}${PipelineTargetConverter.DELIMITER}${commitTarget.commitMessage}"

    @Before
    fun setUp() {
        sut = PipelineTargetConverter()
    }

    @Test
    fun stringToPipelineTarget_unknownTarget_returnsCorrectPipelineTarget() {
        val result = sut.pipelineTargetToString(unknownTarget)
        assertEquals(unknownTargetString, result)
    }

    @Test
    fun stringToPipelineTarget_branchTarget_returnsCorrectPipelineTarget() {
        val result = sut.pipelineTargetToString(branchTarget)
        assertEquals(branchTargetString, result)
    }

    @Test
    fun stringToPipelineTarget_pullRequestTarget_returnsCorrectPipelineTarget() {
        val result = sut.pipelineTargetToString(pullRequestTarget)
        assertEquals(pullRequestTargetString, result)
    }

    @Test
    fun stringToPipelineTarget_commitTarget_returnsCorrectPipelineTarget() {
        val result = sut.pipelineTargetToString(commitTarget)
        assertEquals(commitTargetString, result)
    }

    @Test
    fun pipelineTargetToString_unknownTargetString_returnsUnknownTarget() {
        val result = sut.pipelineTargetToString(unknownTarget)
        assertEquals(unknownTargetString, result)
    }

    @Test
    fun pipelineTargetToString_branchTargetString_returnsBranchTarget() {
        val result = sut.pipelineTargetToString(branchTarget)
        assertEquals(branchTargetString, result)
    }

    @Test
    fun pipelineTargetToString_pullRequestTargetString_returnsPullRequestTarget() {
        val result = sut.pipelineTargetToString(pullRequestTarget)
        assertEquals(pullRequestTargetString, result)
    }

    @Test
    fun pipelineTargetToString_unknownCommitString_returnsCommitTarget() {
        val result = sut.pipelineTargetToString(commitTarget)
        assertEquals(commitTargetString, result)
    }
}