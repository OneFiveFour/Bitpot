package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import net.onefivefour.android.bitpot.data.meta.dummies.CommentDummies
import net.onefivefour.android.bitpot.data.meta.dummies.StringDummies
import net.onefivefour.android.bitpot.data.meta.dummies.WorkspaceDummies
import net.onefivefour.android.bitpot.data.model.CommentPosition
import net.onefivefour.android.bitpot.data.model.converter.CommentConverter
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

class CommentConverterTest : KoinTest {

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

    private lateinit var sut: CommentConverter

    @Before
    fun setup() {
        sut = CommentConverter()
    }

    @Test
    fun toAppModel_pullRequestComment_returnsConvertedComment() {
        val pullRequestComment = CommentDummies.getNetworkComment()
        BitpotData.setSelectedWorkspaceUuid(WorkspaceDummies.getAppWorkspace().uuid)

        val result = sut.toAppModel(pullRequestComment)

        assertEquals(pullRequestComment.id, result.id)
        assertEquals(pullRequestComment.parent?.id, result.parentId)
        assertTrue(result.position is CommentPosition.Line)
        assertEquals(StringDummies.getRawString(), result.content)
        assertEquals(true, result.allowDelete)
        assertEquals(pullRequestComment.user.accountId, result.author.accountId)
        assertEquals(pullRequestComment.pullRequest.id, result.pullRequestId)
    }


    @Test
    fun toNetworkModel_pullRequestComment_returnsConvertedComment() {
        val pullRequestComment = CommentDummies.getPostComment()

        val result = sut.toNetworkModel(pullRequestComment)

        assertEquals(pullRequestComment.content, result.content.raw)
        assertEquals(null, result.inlinePosition)
        assertEquals(pullRequestComment.parentId, result.parent?.id)
    }
}