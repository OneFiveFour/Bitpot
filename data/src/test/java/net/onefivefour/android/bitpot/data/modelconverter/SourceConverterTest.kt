package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.extensions.toMD5
import net.onefivefour.android.bitpot.data.meta.dummies.SourceDummies
import net.onefivefour.android.bitpot.data.model.SourceType
import net.onefivefour.android.bitpot.data.model.converter.SourceConverter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SourceConverterTest {

    private lateinit var sut: SourceConverter

    private val sourceDummy = SourceDummies.getSource()

    @Before
    fun setup() {
        sut = SourceConverter()
    }

    @Test
    fun convert_simpleSource_returnsConvertedSource() {
        val result = sut.toAppModel(sourceDummy)

        val id =(sourceDummy.commit.hash + sourceDummy.path + sourceDummy.size).toMD5()
        val name = sourceDummy.path.substringAfterLast("/")
        val path = sourceDummy.path.removeSuffix(name).removeSuffix("/")

        Assert.assertEquals(id, result.id)
        Assert.assertEquals(path, result.path)
        Assert.assertEquals("", result.refName)
        Assert.assertEquals(SourceType.DIRECTORY, result.type)
        Assert.assertEquals(name, result.name)
    }
}