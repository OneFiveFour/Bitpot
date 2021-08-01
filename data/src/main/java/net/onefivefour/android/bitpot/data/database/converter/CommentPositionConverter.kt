package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import net.onefivefour.android.bitpot.data.model.CommentPosition

/**
 * Database Converter to convert a [CommentPosition] to String and back
 */
class CommentPositionConverter {

    companion object {
        const val PREFIX_FILE = "PREFIX_FILE"
        const val PREFIX_LINE = "PREFIX_LINE"
        const val PREFIX_PULL_REQUEST = "PREFIX_PULL_REQUEST"
    }

    private val gson = Gson()

    @TypeConverter
    fun stringToCommentPosition(string: String): CommentPosition {
        val json: String
        val clazz = when {
            string.startsWith(PREFIX_FILE) -> {
                json = string.removePrefix(PREFIX_FILE)
                CommentPosition.File::class.java
            }
            string.startsWith(PREFIX_PULL_REQUEST) -> {
                json = string.removePrefix(PREFIX_PULL_REQUEST)
                CommentPosition.PullRequest::class.java
            }
            string.startsWith(PREFIX_LINE) -> {
                json = string.removePrefix(PREFIX_LINE)
                CommentPosition.Line::class.java
            }
            else -> throw IllegalArgumentException("Unknown prefix to deserialize CommentPosition: $string")
        }

        return gson.fromJson(json, clazz)
    }



    @TypeConverter
    fun commentPositionToString(position: CommentPosition): String {
        val prefix = when (position) {
            is CommentPosition.File -> PREFIX_FILE
            CommentPosition.PullRequest -> PREFIX_PULL_REQUEST
            is CommentPosition.Line -> PREFIX_LINE
        }
        return prefix + gson.toJson(position)
    }

}