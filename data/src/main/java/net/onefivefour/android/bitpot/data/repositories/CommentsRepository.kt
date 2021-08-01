package net.onefivefour.android.bitpot.data.repositories

import androidx.lifecycle.LiveData
import net.onefivefour.android.bitpot.data.common.NetworkDataConversion
import net.onefivefour.android.bitpot.data.database.CommentsDao
import net.onefivefour.android.bitpot.data.model.Comment
import net.onefivefour.android.bitpot.data.model.PostComment
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.data.model.converter.CommentConverter
import net.onefivefour.android.bitpot.data.model.converter.CommentsConverter
import net.onefivefour.android.bitpot.data.model.converter.UnitConverter
import net.onefivefour.android.bitpot.network.apifields.CommentApiFields
import net.onefivefour.android.bitpot.network.apifields.CommentsApiFields
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit
import net.onefivefour.android.bitpot.network.model.comments.Comment as NetworkComment
import net.onefivefour.android.bitpot.network.model.comments.Comments as NetworkComments

/**
 * The 'Repository' classes should always be the only api contact for the ui layer.
 * The ViewModels can get all their data from here and should not call any other classes directly.
 *
 * This Repository is used to create and delete comments of pull request.
 */
class CommentsRepository(private val commentsDao: CommentsDao) : KoinComponent {


    /**
     * Get the first 100 comments of the given [pullRequestId].
     */
    fun getComments(pullRequestId: Int) = object : NetworkDataConversion<NetworkComments, List<Comment>>() {
        override fun getNetworkCall() = BitbucketService.get(CommentsApiFields()).getComments(pullRequestId)
        override fun getConverter() = CommentsConverter()
        override fun shouldFetch(appData: List<Comment>): Boolean {
            val oldestUpdate = appData.minOf { it.updatedOn }
            return oldestUpdate.isBefore(Instant.now().minus(1, ChronoUnit.SECONDS))
        }

        override fun cacheData(appData: List<Comment>) = commentsDao.insert(appData)
        override fun loadCachedData() = commentsDao.getByPullRequestId(pullRequestId)
    }.get()

    /**
     * Creates a new comment for the given pullRequestId
     *
     * @param pullRequestId the id of the pull request that should get the new comment
     * @param newComment the POST version of a comment. This is sent to the API.
     *
     * @return A [Resource] holding information about the request status.
     * If successful, this resource will hold the complete [Comment] that was just created.
     */
    fun createComment(pullRequestId: Int, newComment: PostComment): LiveData<Resource<Comment>> {
        val postComment = CommentConverter().toNetworkModel(newComment)
        return object : NetworkDataConversion<NetworkComment, Comment>() {
            override fun getNetworkCall() = BitbucketService.get(CommentApiFields()).postComment(pullRequestId, postComment)
            override fun getConverter() = CommentConverter()
            override fun processData(appData: Comment) = commentsDao.insert(appData)
        }.get()
    }


    /**
     * Deletes the given comment.
     */
    fun deleteComment(pullRequestId: Int, commentId: Int): LiveData<Resource<Unit>> {
        return object : NetworkDataConversion<Unit, Unit>() {
            override fun getNetworkCall() = BitbucketService.get().deleteComment(pullRequestId, commentId)
            override fun getConverter() = UnitConverter()
            override fun reactToEmptyResponse() = commentsDao.delete(commentId)
        }.get()
    }
}