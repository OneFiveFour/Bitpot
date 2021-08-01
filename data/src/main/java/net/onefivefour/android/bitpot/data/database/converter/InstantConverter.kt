package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import org.threeten.bp.Instant

/**
 * Database Converter to convert an [Instant] to String and back
 */
class InstantConverter {
    @TypeConverter
    fun intToInstant(epochSecond: Long): Instant {
        return Instant.ofEpochSecond(epochSecond)
    }

    @TypeConverter
    fun instantToLong(instant: Instant): Long {
        return instant.epochSecond
    }
}