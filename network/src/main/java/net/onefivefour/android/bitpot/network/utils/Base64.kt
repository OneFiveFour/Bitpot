package net.onefivefour.android.bitpot.network.utils

import java.io.ByteArrayOutputStream

/**
 * Extension method for a ByteArray to encode it into a String.
 */
fun ByteArray.encodeBase64ToString(): String = String(this.encodeBase64())

/**
 * Extension method for a ByteArray to encode it into Base64 format.
 */
fun ByteArray.encodeBase64(): ByteArray {
    val table = (CharRange('A', 'Z') + CharRange('a', 'z') + CharRange('0', '9') + '+' + '/').toCharArray()
    val output = ByteArrayOutputStream()
    var padding = 0
    var position = 0
    while (position < this.size) {
        var b = this[position].toInt() and 0xFF shl 16 and 0xFFFFFF
        if (position + 1 < this.size) b = b or (this[position + 1].toInt() and 0xFF shl 8) else padding++
        if (position + 2 < this.size) b = b or (this[position + 2].toInt() and 0xFF) else padding++
        (0 until 4 - padding).forEach { _ ->
            val c = b and 0xFC0000 shr 18
            output.write(table[c].code)
            b = b shl 6
        }
        position += 3
    }
    (0 until padding).forEach { _ ->
        output.write('='.code)
    }
    return output.toByteArray()
}