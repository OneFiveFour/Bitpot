package net.onefivefour.android.bitpot.data.database.converter

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.threeten.bp.Instant

class InstantConverterTest {

    // 01.01.1970 - 01:00:00
    private val zeroEpochSeconds = 0L
    private val zeroInstant = Instant.ofEpochSecond(zeroEpochSeconds)

    // 26.10.2019 - 22:39:51
    private val dateEpochSeconds = 1572122391L
    private val dateInstant = Instant.ofEpochSecond(dateEpochSeconds)

    private lateinit var sut: InstantConverter


    @Before
    fun setUp() {
        sut = InstantConverter()
    }

    @Test
    fun intToInstant_givenZeroEpoch_returnZeroInstant() {
        val result = sut.intToInstant(zeroEpochSeconds)
        assertEquals(zeroInstant, result)
    }

    @Test
    fun intToInstant_givenDateEpoch_returnDateInstant() {
        val result = sut.intToInstant(dateEpochSeconds)
        assertEquals(dateInstant, result)
    }

    @Test
    fun instantToLong_givenZeroInstant_returnZeroEpoch() {
        val result = sut.instantToLong(zeroInstant)
        assertEquals(zeroEpochSeconds, result)
    }

    @Test
    fun instantToLong_givenDateInstant_returnDateInstant() {
        val result = sut.instantToLong(dateInstant)
        assertEquals(dateEpochSeconds, result)
    }
}