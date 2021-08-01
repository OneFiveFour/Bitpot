package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import net.onefivefour.android.bitpot.data.meta.dummies.RepositoryDummies
import net.onefivefour.android.bitpot.data.model.PipelineState
import net.onefivefour.android.bitpot.data.model.Repository
import net.onefivefour.android.bitpot.data.model.RepositoryAvatar
import net.onefivefour.android.bitpot.data.model.converter.RepositoryConverter
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class RepositoryConverterTest: KoinTest {

    private lateinit var sut: RepositoryConverter

    private val repositoryDummy = RepositoryDummies.getRepository()

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

    @Before
    fun setup() {
        sut = RepositoryConverter()
    }

    @Test
    fun convert_simpleRepository_returnsConvertedRepository() {
        val result = sut.toAppModel(repositoryDummy)

        assertEquals("Repository.name", result.name)
        assertEquals("Repository.uuid", result.uuid)
        assertEquals(false, result.isPrivate)
        assertEquals(1572278556L, result.lastUpdated.epochSecond)
        assertEquals(PipelineState.UNKNOWN, result.lastPipelineState)
        assertThat(result, instanceOf(Repository::class.java))
        assertThat(result.avatar, instanceOf(RepositoryAvatar::class.java))

    }
}