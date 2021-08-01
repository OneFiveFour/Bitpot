package net.onefivefour.android.bitpot.databinding

import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.noties.markwon.Markwon
import org.threeten.bp.Instant
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow


/**
 * Use the given [Instant] to set the text to a human readable String
 * which tells the user how long in the past the given Instant happened.
 */
@BindingAdapter("humanizedAgo")
fun setHumanizedAgo(textView: TextView, dateTime: Instant?) {
    if (dateTime == null) return

    val millis = dateTime.toEpochMilli()
    val humanizedAgo = DateUtils.getRelativeTimeSpanString(millis).toString()
    textView.text = humanizedAgo
}

@BindingAdapter("humanizedFileSize")
fun setHumanizedFileSize(textView: TextView, fileSize: Long?) {
    if (fileSize == null || fileSize <= 0) {
        textView.text = "0"
        return
    }

    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (log10(fileSize.toDouble()) / log10(1024.0)).toInt()
    val number = fileSize / 1024.0.pow(digitGroups.toDouble())
    val result = DecimalFormat("#,##0.#").format(number).toString() + " " + units[digitGroups]

    textView.text = result
}

@BindingAdapter("markdown")
fun setMarkDown(textView: TextView, markdown: String?) {
    if (markdown == null) return

    val markwon = Markwon.create(textView.context)
    markwon.setMarkdown(textView, markdown)
}