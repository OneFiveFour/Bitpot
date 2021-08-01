package net.onefivefour.android.bitpot.network.utils

import org.junit.Test
import kotlin.test.assertEquals


internal class Base64KtTest {

    companion object {
        const val SAMPLE_STRING = "This is a sample for base64"
        const val SAMPLE_STRING_BASE64 = "VGhpcyBpcyBhIHNhbXBsZSBmb3IgYmFzZTY0"

        val SAMPLE_BYTE_ARRAY = byteArrayOf(0x1, 0x2, 0x3, 0x4)
    }


    @Test
    fun encodeBase64ToString_emptyString_returnsEmptyString() {
        val result = "".toByteArray().encodeBase64ToString()

        assertEquals("", result)
    }
    @Test
    fun encodeBase64ToString_sampleString_returnsBase64String() {
        val result = SAMPLE_STRING.toByteArray().encodeBase64ToString()

        assertEquals(SAMPLE_STRING_BASE64, result)
    }

    @Test
    fun encodeBase64_emptyString_returnsEmptyByteArray() {
        val result = "".toByteArray().encodeBase64()

        assertEquals(0, result.size)
    }

    @Test
    fun encodeBase64_sampleString_returnsBase64String() {
        val result = SAMPLE_BYTE_ARRAY.encodeBase64()

        assertEquals(result[0], 65)
        assertEquals(result[1], 81)
        assertEquals(result[2], 73)
        assertEquals(result[3], 68)
        assertEquals(result[4], 66)
        assertEquals(result[5], 65)
        assertEquals(result[6], 61)
        assertEquals(result[7], 61)
    }

}