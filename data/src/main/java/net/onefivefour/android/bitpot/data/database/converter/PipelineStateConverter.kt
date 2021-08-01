package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import net.onefivefour.android.bitpot.data.model.PipelineState

/**
 * Database Converter to convert a [PipelineState] to String and back
 */
class PipelineStateConverter {
    @TypeConverter
    fun stringToPipelineState(string: String): PipelineState {
        return PipelineState.valueOf(string)
    }

    @TypeConverter
    fun pipelineStateToString(pipelineState: PipelineState): String {
        return pipelineState.name
    }
}