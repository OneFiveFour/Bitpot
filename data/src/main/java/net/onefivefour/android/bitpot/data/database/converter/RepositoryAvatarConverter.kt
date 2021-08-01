package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import net.onefivefour.android.bitpot.data.model.RepositoryAvatar
import java.util.regex.Pattern

/**
 * Database Converter to convert a [RepositoryAvatar] to String and back
 */
class RepositoryAvatarConverter {

    companion object {
        const val CUSTOM_DELIMITER = ","
    }

    @TypeConverter
    fun stringToRepositoryAvatar(string: String): RepositoryAvatar {
        // check for right pattern
        val regEx = "\\d+$CUSTOM_DELIMITER\\d+$CUSTOM_DELIMITER\\d+$CUSTOM_DELIMITER\\d+"
        return if (Pattern.compile(regEx).matcher(string).matches()) {
            val (drawableString, gradientFromColorString, gradientToColorString, textColorString) = string.split(CUSTOM_DELIMITER)
            val drawable = Integer.parseInt(drawableString)
            val gradientFromColor = Integer.parseInt(gradientFromColorString)
            val gradientToColor = Integer.parseInt(gradientToColorString)
            val textColor = Integer.parseInt(textColorString)
            RepositoryAvatar.Language(drawable, gradientFromColor, gradientToColor, textColor)
        } else {
            // regex could not match -> treat it as url
            RepositoryAvatar.Image(string)
        }
    }

    @TypeConverter
    fun repositoryAvatarToString(repositoryAvatar: RepositoryAvatar): String {
        return when (repositoryAvatar) {
            is RepositoryAvatar.Language ->
                repositoryAvatar.drawable.toString() +
                        CUSTOM_DELIMITER +
                        repositoryAvatar.gradientFromColor.toString() +
                        CUSTOM_DELIMITER +
                        repositoryAvatar.gradientToColor.toString() +
                        CUSTOM_DELIMITER +
                        repositoryAvatar.textColor.toString()

            is RepositoryAvatar.Image -> repositoryAvatar.url
        }
    }

}