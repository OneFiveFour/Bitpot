package net.onefivefour.android.bitpot.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.onefivefour.android.bitpot.data.model.WebHookEvent


/**
 * Database Converter to convert a [WebHookEvent] to String and back
 */
class WebHookEventsConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToWebHookEvent(string: String): List<WebHookEvent> {
        val typeToken = object : TypeToken<List<WebHookEvent?>?>() {}
        return gson.fromJson(string, typeToken.type)
    }

    @TypeConverter
    fun webHookEventToString(webHookEvents: List<WebHookEvent>): String {
        return gson.toJson(webHookEvents)
    }
}