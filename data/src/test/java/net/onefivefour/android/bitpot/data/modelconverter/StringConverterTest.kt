package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.meta.dummies.StringDummies
import net.onefivefour.android.bitpot.data.model.converter.StringConverter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StringConverterTest {

    private lateinit var sut: StringConverter

    private val stringDummy = StringDummies.getSimpleString()

    @Before
    fun setup() {
        sut = StringConverter()
    }

    @Test
    fun convert_simpleSource_returnsConvertedSource() {
        val result = sut.toAppModel(stringDummy)

        Assert.assertEquals(stringDummy, result)
    }
}