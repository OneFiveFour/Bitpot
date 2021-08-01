package net.onefivefour.android.bitpot.data.common

import net.onefivefour.android.bitpot.data.meta.dummies.CommentAuthorDummies
import net.onefivefour.android.bitpot.data.model.Comment
import net.onefivefour.android.bitpot.data.model.CommentPosition
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.Instant

class CommentSorterTest {

    @Test
    fun topologicalSort_returnsCorrectSortingOrder() {

        val allowDelete = true
        val author = CommentAuthorDummies.getSimpleCommentAuthor()
        val content = "asdf"
        val createdOn = Instant.now()
        val updatedOn = Instant.now()
        val pullRequestId = 1
        val position = CommentPosition.PullRequest

        val comments = listOf(
            Comment(6, 5, pullRequestId, content, author, position, createdOn, updatedOn, allowDelete),
            Comment(4, 1, pullRequestId, content, author, position, createdOn, updatedOn, allowDelete),
            Comment(2, null, pullRequestId, content, author, position, createdOn, updatedOn, allowDelete),
            Comment(1, null, pullRequestId, content, author, position, createdOn, updatedOn, allowDelete),
            Comment(5, null, pullRequestId, content, author, position, createdOn, updatedOn, allowDelete),
            Comment(3, 1, pullRequestId, content, author, position, createdOn, updatedOn, allowDelete),
            Comment(7, 2, pullRequestId, content, author, position, createdOn, updatedOn, allowDelete),
            Comment(8, 4, pullRequestId, content, author, position, createdOn, updatedOn, allowDelete)
        )

        val sortedComments = CommentSorter.topologicalSort(comments)

        val ids = sortedComments.map { it.id }

        Assert.assertEquals(1, ids[0])
        Assert.assertEquals(3, ids[1])
        Assert.assertEquals(4, ids[2])
        Assert.assertEquals(8, ids[3])
        Assert.assertEquals(2, ids[4])
        Assert.assertEquals(7, ids[5])
        Assert.assertEquals(5, ids[6])
        Assert.assertEquals(6, ids[7])
    }
}