package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.model.converter.UnitConverter
import org.junit.Before
import org.junit.Test

class UnitConverterTest {

    private lateinit var sut: UnitConverter

    @Before
    fun setup() {
        sut = UnitConverter()
    }

    @Test
    fun toAppModel_returnsUnit() {
        val unit = Unit
        val result = sut.toAppModel(unit)
        assert(result == unit)
    }
}