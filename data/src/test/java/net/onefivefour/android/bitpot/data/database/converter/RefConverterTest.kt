package net.onefivefour.android.bitpot.data.database.converter

import com.google.gson.JsonSyntaxException
import net.onefivefour.android.bitpot.data.meta.dummies.RefDummies
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class RefConverterTest {

    private lateinit var sut: RefConverter


    @Before
    fun setUp() {
        sut = RefConverter()
    }

    @Test
    fun stringToRef_givenEmptyString_throwsIllegalStateException() {
        assertFailsWith<NullPointerException> {
            sut.stringToRef("")
        }
    }

    @Test
    fun stringToRef_givenWrongString_throwsIllegalStateException() {
        assertFailsWith<JsonSyntaxException> {
            sut.stringToRef("asdf")
        }
    }

    @Test
    fun stringToRef_givenCorrectString_returnsCorrectRef() {
        val json = RefDummies.getSerializedSimpleBranchRef()

        val result = sut.stringToRef(json)

        assertEquals(RefDummies.getSimpleAppBranchRef(), result)
    }

    @Test
    fun refToString_givenCorrectRef_returnsCorrectString() {
        val ref = RefDummies.getSimpleAppBranchRef()

        val result = sut.refToString(ref)

        assertEquals(RefDummies.getSerializedSimpleBranchRef(), result)
    }

    @Test
    fun refToString_givenWrongRef_returnsWrongString() {
        val ref = RefDummies.getSimpleAppTagRef()

        val result = sut.refToString(ref)

        assertNotEquals(RefDummies.getSerializedSimpleBranchRef(), result)
    }
}