package net.onefivefour.android.bitpot.data.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsKtTest {

    private val dateString = "2019-04-01T16:24:11.252+05:30"

    @Test
    fun toInstant() {
        val result = dateString.toInstant()
        assertEquals(1554116051L, result.epochSecond)
    }

    @Test
    fun toMd5() {
        val result = "test".toMD5()

        assertEquals("098f6bcd4621d373cade4e832627b4f6", result)
    }

    @Test
    fun toHex() {
        val result = byteArrayOf(0x12, 0x23, 0x34).toHex()

        assertEquals("122334", result)
    }
}