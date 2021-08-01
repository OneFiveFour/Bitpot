package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import net.onefivefour.android.bitpot.data.model.Ref

/**
 * Database Converter to convert a [Ref] to String and back
 */
class RefConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToRef(string: String): Ref {
       return gson.fromJson(string, Ref::class.java)
    }



    @TypeConverter
    fun refToString(ref: Ref): String {
        return gson.toJson(ref)
    }

}