package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import net.onefivefour.android.bitpot.data.model.RefType

/**
 * Database Converter to convert a [RefType] to String and back
 */
class RefsTypeConverter {
    @TypeConverter
    fun stringToRefType(string: String): RefType {
        return RefType.valueOf(string)
    }

    @TypeConverter
    fun refTypeToString(refType: RefType): String {
        return refType.name
    }
}