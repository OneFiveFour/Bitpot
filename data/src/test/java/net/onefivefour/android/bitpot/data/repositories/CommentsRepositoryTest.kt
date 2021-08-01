package net.onefivefour.android.bitpot.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import io.mockk.mockk
import net.onefivefour.android.bitpot.data.database.CommentsDao
import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import net.onefivefour.android.bitpot.data.meta.dummies.CommentDummies
import net.onefivefour.android.bitpot.data.model.PostComment
import org.junit.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class CommentsRepositoryTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Force tests to be executed synchronously

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

    private val commentsDaoMock: CommentsDao = mockk()

    private lateinit var sut : CommentsRepository

    @Before
    fun setup() {
        sut = CommentsRepository(commentsDaoMock)
    }

    @Test
    fun createComment_returnsLiveData() {
        val postComment: PostComment = CommentDummies.getPostComment()
        sut.createComment(1, postComment).test().assertHasValue()
    }

    @Test
    fun deleteComment_returnsLiveData() {
        val postComment: PostComment = CommentDummies.getPostComment()
        sut.deleteComment(1, 2).test().assertHasValue()
    }
}