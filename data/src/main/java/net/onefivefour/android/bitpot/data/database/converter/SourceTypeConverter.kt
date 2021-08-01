package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import net.onefivefour.android.bitpot.data.model.SourceType

/**
 * Database Converter to convert a [SourceType] to String and back
 */
class SourceTypeConverter {

    companion object {
        /**
         * To make database ORDER BY statements sort [SourceType]
         * by the order the enum values are defined instead of their
         * alphabetic order, we prepend their ordinal number to the
         * database entries.
         *
         * Filtering out this number is done by this Converter class.
         */
        const val ORDER_DELIMITER = "."
    }

    @TypeConverter
    fun stringToSourceType(string: String): SourceType {
        val enumName = string.split(ORDER_DELIMITER)[1]
        return SourceType.valueOf(enumName)
    }

    @TypeConverter
    fun sourceTypeToString(sourceType: SourceType): String {
        return "${sourceType.ordinal}$ORDER_DELIMITER${sourceType.name}"
    }
}