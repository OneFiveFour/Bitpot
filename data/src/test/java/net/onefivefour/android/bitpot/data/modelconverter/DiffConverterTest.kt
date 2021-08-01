package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import net.onefivefour.android.bitpot.data.meta.FileLoader
import net.onefivefour.android.bitpot.data.model.converter.DiffConverter
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class DiffConverterTest : KoinTest {
    companion object {

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            startKoin { modules(dataModuleTesting) }
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            stopKoin()
        }
    }


    private lateinit var sut: DiffConverter

    @Before
    fun setup() {
        sut = DiffConverter()
    }

    @Test
    fun toAppModel_correctDiffString_returnsCorrectAppModel() {
        val diff = FileLoader.getJson("git_diff_2.txt")

        val result = sut.toAppModel(diff)

        assertEquals(34, result.size)
    }
}