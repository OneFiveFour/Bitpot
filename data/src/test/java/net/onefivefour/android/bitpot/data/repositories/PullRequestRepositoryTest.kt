package net.onefivefour.android.bitpot.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.data.database.PullRequestsDao
import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import net.onefivefour.android.bitpot.data.meta.dummies.PostMergeDummies
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class PullRequestRepositoryTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Force tests to be executed synchronously

    companion object {

        const val PULL_REQUEST_ID = 1

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
    
    private val pullRequestDaoMock : PullRequestsDao = mockk()

    private val sut = PullRequestRepository(pullRequestDaoMock)

    @Test
    fun getPullRequestDetails_returnsLiveData() {
        sut.getPullRequestDetails(PULL_REQUEST_ID).test().assertHasValue()
    }

    @Test
    fun approve_returnsLiveData() {
        sut.approve(PULL_REQUEST_ID).test().assertHasValue()
    }

    @Test
    fun unapprove_returnsLiveData() {
        sut.unapprove(PULL_REQUEST_ID).test().assertHasValue()
    }

    @Test
    fun merge_returnsLiveData() {
        val postMerge = PostMergeDummies.getPostMerge()
        sut.merge(PULL_REQUEST_ID, postMerge).test().assertHasValue()
    }
}