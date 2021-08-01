package net.onefivefour.android.bitpot.data.extensions

import net.onefivefour.android.bitpot.data.common.CommentSorter
import net.onefivefour.android.bitpot.data.model.Comment
import org.threeten.bp.Instant
import org.threeten.bp.ZonedDateTime
import java.security.MessageDigest

fun String.toInstant(): Instant = ZonedDateTime.parse(this).toInstant()

fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

/**
 * sort a list of [Comment]s topologically by their id/parentId relationship
 */
fun List<Comment>.sort(): List<Comment> {
    return CommentSorter.topologicalSort(this)
}