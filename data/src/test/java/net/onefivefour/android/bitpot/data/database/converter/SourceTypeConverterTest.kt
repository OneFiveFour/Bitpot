package net.onefivefour.android.bitpot.data.database.converter

import net.onefivefour.android.bitpot.data.model.SourceType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class SourceTypeConverterTest {

    private lateinit var sut: SourceTypeConverter


    @Before
    fun setUp() {
        sut = SourceTypeConverter()
    }

    @Test
    fun stringToSourceType_givenEmptyString_throwsIndexOutOfBoundsException() {
        assertFailsWith<IndexOutOfBoundsException> {
            sut.stringToSourceType("")
        }
    }

    @Test
    fun stringToSourceType_givenWrongString_throwsIndexOutOfBoundsException() {
        assertFailsWith<IndexOutOfBoundsException> {
            sut.stringToSourceType("asdf")
        }
    }

    @Test
    fun stringToSourceType_givenCorrectString_returnsCorrectSourceType() {
        val serializedSourceType = "0${SourceTypeConverter.ORDER_DELIMITER}FOLDER_UP"

        val result = sut.stringToSourceType(serializedSourceType)

        assertEquals(SourceType.FOLDER_UP, result)
    }

    @Test
    fun sourceTypeToString_givenSourceType_returnsCorrectString() {
        val result = sut.sourceTypeToString(SourceType.FOLDER_UP)

        assertEquals("0${SourceTypeConverter.ORDER_DELIMITER}FOLDER_UP", result)
    }

}