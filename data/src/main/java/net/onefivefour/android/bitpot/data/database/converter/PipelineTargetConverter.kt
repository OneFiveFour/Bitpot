package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import net.onefivefour.android.bitpot.data.model.PipelineTarget
import net.onefivefour.android.bitpot.data.model.PipelineTargetType
import timber.log.Timber

/**
 * Database Converter to convert a [PipelineTarget] to String and back
 */
class PipelineTargetConverter {

    companion object {
        const val DELIMITER = "$$$"
    }

    @TypeConverter
    fun stringToPipelineTarget(string: String): PipelineTarget {
        val typeString = string.subSequence(0, string.indexOf(DELIMITER)).toString()
        return try {
            when (PipelineTargetType.valueOf(typeString)) {
                PipelineTargetType.BRANCH -> toBranchTarget(string)
                PipelineTargetType.PULL_REQUEST -> toPullRequestTarget(string)
                PipelineTargetType.COMMIT -> toCommitTarget(string)
                PipelineTargetType.UNKNOWN -> PipelineTarget.Unknown
            }
        } catch (exception: IllegalArgumentException) {
            Timber.e("Cannot find a PipelineTargetType for value $typeString")
            PipelineTarget.Unknown
        }
    }

    private fun toCommitTarget(string: String): PipelineTarget {
        val (_, commitHash) = string.split(DELIMITER)
        return PipelineTarget.Commit(commitHash)
    }

    private fun toPullRequestTarget(string: String): PipelineTarget {
        val (_, source, destination) = string.split(DELIMITER)
        return PipelineTarget.PullRequest(source, destination)
    }

    private fun toBranchTarget(string: String): PipelineTarget {
        val (_, branchName) = string.split(DELIMITER)
        return PipelineTarget.Branch(branchName)
    }

    @TypeConverter
    fun pipelineTargetToString(target: PipelineTarget): String {
        return when (target) {
            is PipelineTarget.Branch -> "${target.type}$DELIMITER${target.branchName}"
            is PipelineTarget.PullRequest -> "${target.type}$DELIMITER${target.source}$DELIMITER${target.destination}"
            is PipelineTarget.Commit -> "${target.type}$DELIMITER${target.commitMessage}"
            PipelineTarget.Unknown -> "${target.type}$DELIMITER" // put delimiter at the end to allow splitting
        }
    }

}