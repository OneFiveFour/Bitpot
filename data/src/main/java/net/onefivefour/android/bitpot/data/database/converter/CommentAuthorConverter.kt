package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import net.onefivefour.android.bitpot.data.model.CommentAuthor

/**
 * Database Converter to convert a [CommentAuthor] to String and back
 */
class CommentAuthorConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToCommentAuthor(string: String): CommentAuthor {
        return gson.fromJson(string, CommentAuthor::class.java)
    }

    @TypeConverter
    fun commentAuthorToString(ref: CommentAuthor): String {
        return gson.toJson(ref)
    }

}