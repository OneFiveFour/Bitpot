package net.onefivefour.android.bitpot.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute (attrRes, typedValue, true)
    return typedValue.data
}

fun Context.readFileFromAssets(fileUrl: String): String? {
    return try {
        val inputStream = this.assets.open(fileUrl)
        inputStream.use { input ->
            val bufferedReader = BufferedReader(InputStreamReader(input))
            val content = StringBuilder(input.available())
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                content.append(line)
                content.append(System.getProperty("line.separator"))
            }
            content.toString()
        }
    } catch (ex: IOException) {
        Timber.e("Error while reading asset from file: $ex")
        null
    }
}