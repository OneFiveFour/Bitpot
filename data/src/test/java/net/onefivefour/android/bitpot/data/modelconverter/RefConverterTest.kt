package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.meta.dummies.RefDummies
import net.onefivefour.android.bitpot.data.model.RefType
import net.onefivefour.android.bitpot.data.model.converter.RefConverter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RefConverterTest {

    private lateinit var sut: RefConverter

    private val refDummy = RefDummies.getSimpleNetworkBranchRef()

    @Before
    fun setup() {
        sut = RefConverter()
    }

    @Test
    fun convert_simpleRef_returnsConvertedRef() {
        val result = sut.toAppModel(refDummy)

        Assert.assertEquals("9e9051c99cd44a51818598b7811a6fa9", result.id)
        Assert.assertEquals("Repository.uuid", result.repoUuid)
        Assert.assertEquals("Ref.name", result.name)
        Assert.assertEquals("hash", result.hash)
        Assert.assertEquals(RefType.BRANCH, result.type)

    }
}
