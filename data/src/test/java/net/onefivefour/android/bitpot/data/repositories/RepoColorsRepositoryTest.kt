package net.onefivefour.android.bitpot.data.repositories

import androidx.lifecycle.MediatorLiveData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.data.database.RepositoryColorsDao
import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import net.onefivefour.android.bitpot.data.meta.CoroutineTestRule
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class RepoColorsRepositoryTest {

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    companion object {
        const val REPOSITORY_UUID = "repository_uuid"

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

    private lateinit var sut: RepoColorsRepository

    private val repoColorDaoMock: RepositoryColorsDao = mockk {
        every { getByRepositoryUuid(any()) } returns MediatorLiveData()
    }

    @Before
    fun setup() {
        sut = RepoColorsRepository(repoColorDaoMock)
    }

    @Test
    fun getRepositoryColors_daoIsCalledWithCorrectParameter() {
        sut.getRepositoryColors(REPOSITORY_UUID)
        verify { repoColorDaoMock.getByRepositoryUuid(REPOSITORY_UUID) }
    }
}