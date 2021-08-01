package net.onefivefour.android.bitpot.data.database.converter

import net.onefivefour.android.bitpot.data.meta.dummies.RefTypeDummies
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class RefTypeConverterTest {

    private lateinit var sut: RefsTypeConverter


    @Before
    fun setUp() {
        sut = RefsTypeConverter()
    }

    @Test
    fun stringToRefType_givenEmptyString_throwsIllegalArgumentException() {
        assertFailsWith<IllegalArgumentException> {
            sut.stringToRefType("")
        }
    }

    @Test
    fun stringToRefType_givenWrongString_throwsIllegalArgumentException() {
        assertFailsWith<IllegalArgumentException> {
            sut.stringToRefType("asdf")
        }
    }

    @Test
    fun stringToRef_givenCorrectString_returnsCorrectRefType() {
        val json = RefTypeDummies.getSerializedTagRefType()

        val result = sut.stringToRefType(json)

        assertEquals(RefTypeDummies.getTagRefType(), result)
    }

    @Test
    fun refToString_givenCorrectRefType_returnsCorrectString() {
        val ref = RefTypeDummies.getTagRefType()

        val result = sut.refTypeToString(ref)

        assertEquals(RefTypeDummies.getSerializedTagRefType(), result)
    }

    @Test
    fun refToString_givenWrongRefType_returnsWrongString() {
        val ref = RefTypeDummies.getBranchRefType()

        val result = sut.refTypeToString(ref)

        assertNotEquals(RefTypeDummies.getSerializedTagRefType(), result)
    }
}