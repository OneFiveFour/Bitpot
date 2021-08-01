package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import net.onefivefour.android.bitpot.data.model.PullRequestState

/**
 * Database Converter to convert a [PullRequestState] to String and back
 */
class PullRequestStateConverter {
    @TypeConverter
    fun stringToPullRequestState(string: String): PullRequestState {
        return PullRequestState.valueOf(string)
    }

    @TypeConverter
    fun pullRequestStateToString(pipelineState: PullRequestState): String {
        return pipelineState.name
    }
}