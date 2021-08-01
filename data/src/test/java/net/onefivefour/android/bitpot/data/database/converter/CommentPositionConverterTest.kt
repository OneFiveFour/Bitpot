package net.onefivefour.android.bitpot.data.database.converter

import net.onefivefour.android.bitpot.data.meta.dummies.CommentPositionDummies
import net.onefivefour.android.bitpot.data.model.CommentPosition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CommentPositionConverterTest {

    private lateinit var sut: CommentPositionConverter

    @Before
    fun setUp() {
        sut = CommentPositionConverter()
    }

    @Test
    fun stringToCommentPosition_correctPullRequestCommentPosition_returnsCorrectString() {
        val commentPosition = CommentPositionDummies.getPullRequestCommentPosition()

        val result = sut.commentPositionToString(commentPosition)

        assertTrue(result.contains(CommentPositionConverter.PREFIX_PULL_REQUEST))
    }

    @Test
    fun commentPositionToString_correctString_returnsPullRequestCommentPosition() {
        val string = "PREFIX_PULL_REQUEST{}"

        val result = sut.stringToCommentPosition(string)

        assertTrue(result is CommentPosition.PullRequest)
    }

    @Test
    fun stringToCommentPosition_correctFileCommentPosition_returnsCorrectString() {
        val commentPosition = CommentPositionDummies.getFileCommentPosition()

        val result = sut.commentPositionToString(commentPosition)

        assertTrue(result.contains(CommentPositionConverter.PREFIX_FILE))
        assertTrue(result.contains(commentPosition.filePath))
    }

    @Test
    fun commentPositionToString_correctString_returnsFileCommentPosition() {
        val string = "PREFIX_FILE{\"filePath\":\"app/src/main/res/drawable/ic_launcher_background.json\"}"

        val result = sut.stringToCommentPosition(string)

        assertTrue(result is CommentPosition.File)
        if (result is CommentPosition.File) {
            assertEquals("app/src/main/res/drawable/ic_launcher_background.json", result.filePath)
        }
    }

    @Test
    fun stringToCommentPosition_correctLineCommentPosition_returnsCorrectString() {
        val commentPosition = CommentPositionDummies.getLineCommentPosition()

        val result = sut.commentPositionToString(commentPosition)

        assertTrue(result.contains(CommentPositionConverter.PREFIX_LINE))
        assertTrue(result.contains(commentPosition.filePath))
        assertTrue(result.contains(commentPosition.sourceLine.toString()))
        assertTrue(result.contains(commentPosition.destinationLine.toString()))
    }

    @Test
    fun commentPositionToString_correctString_returnsLineCommentPosition() {
        val string = "PREFIX_LINE{\"destinationLine\":14,\"filePath\":\"app/src/main/AndroidManifest.xml\",\"sourceLine\":12}"

        val result = sut.stringToCommentPosition(string)

        assertTrue(result is CommentPosition.Line)
        if (result is CommentPosition.Line) {
            assertEquals("app/src/main/AndroidManifest.xml", result.filePath)
            assertEquals(12, result.sourceLine)
            assertEquals(14, result.destinationLine)
        }
    }
}