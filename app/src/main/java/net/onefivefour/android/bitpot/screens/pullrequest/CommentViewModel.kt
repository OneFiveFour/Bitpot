package net.onefivefour.android.bitpot.screens.pullrequest

import androidx.lifecycle.*
import net.onefivefour.android.bitpot.data.model.Comment
import net.onefivefour.android.bitpot.data.model.PostComment
import net.onefivefour.android.bitpot.data.model.ResourceStatus
import net.onefivefour.android.bitpot.data.repositories.CommentsRepository

/**
 * This ViewModel handles comments of pull requests.
 *
 * Use this ViewModel to either get all comments of a certain PR or to create a comment
 * in this PR.
 *
 * In [PullRequestActivity], the LiveData of the method [getComments] is fed directly into the [DiffViewModel].
 * There the comments are compiled into the actual merge diff.
 */
class CommentViewModel(private val commentsRepository: CommentsRepository) : ViewModel() {

    /**
     * The current pull request id
     */
    private var pullRequestId = MutableLiveData<Int>()
    
    
    private val pullRequestIdChanged = Transformations.switchMap(pullRequestId) { id ->
        commentsRepository.getComments(id)
    }

    /**
     * Used to trigger a complete refresh of all comments of the current PR
     */
    private val refreshTrigger = MutableLiveData<Unit>()
    
    
    private val refreshTriggered = Transformations.switchMap(refreshTrigger) {
        val pullRequestId = pullRequestId.value ?: return@switchMap null
        commentsRepository.getComments(pullRequestId)
    }

    /**
     * Observe this LiveData to get a list of all comments in the current pull request
     */
    fun getComments() = MediatorLiveData<List<Comment>>().apply {
        addSource(refreshTriggered) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> resource.data ?: emptyList()
                ResourceStatus.ERROR -> emptyList()
                ResourceStatus.LOADING -> emptyList()
            }
        }
        addSource(pullRequestIdChanged) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> resource.data ?: emptyList()
                ResourceStatus.ERROR -> emptyList()
                ResourceStatus.LOADING -> emptyList()
            }
        }
    }

    /**
     * Observe this LiveData to get updates that may be of interest for the UI
     */
    fun getCommentUiEvents(): LiveData<UiEvent> = MediatorLiveData<UiEvent>().apply {
        addSource(createCommentCall) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> {
                    refresh()
                    UiEvent.CommentSendSuccess
                }
                ResourceStatus.ERROR -> UiEvent.CommentSendError(resource.message)
                ResourceStatus.LOADING -> UiEvent.CommentSendLoading
            }
        }
        addSource(deleteCommentCall) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> UiEvent.CommentDeleteSuccess
                ResourceStatus.ERROR -> UiEvent.CommentDeleteError(resource.message)
                ResourceStatus.LOADING -> UiEvent.CommentDeleteLoading
            }
        }
    }

    /**
     * set this to a [PostComment] to trigger the creation of a new comment for the current PR
     */
    private val createComment = MutableLiveData<PostComment>()

    /**
     * post a commentId on this LiveData to trigger the deletion of it
     */
    private val deleteCommentId = MutableLiveData<Int>()

    /**
     * The LiveData of the status of the API call to create a new comment
     */
    private val createCommentCall = Transformations.switchMap(createComment) { postComment ->
        val pullRequestId = pullRequestId.value ?: throw AssertionError("Could not create comment, because pullRequestId was not set. Should not be possible")
        commentsRepository.createComment(pullRequestId, postComment)
    }

    /**
     * The LiveData of the status of the API call to delete a comment
     */
    private val deleteCommentCall = Transformations.switchMap(deleteCommentId) { commentId ->
        val pullRequestId = pullRequestId.value ?: throw AssertionError("Could not delete comment, because pullRequestId was not set. Should not be possible")
        commentsRepository.deleteComment(pullRequestId, commentId)
    }

    /**
     * Call this method to send a newComment to Bitbucket.
     */
    fun createComment(comment: PostComment) = createComment.postValue(comment)

    /**
     * Call this method to delete the comment with the given id
     */
    fun deleteComment(commentId: Int) = deleteCommentId.postValue(commentId)

    /**
     * Call this method to set a new PullRequestId
     */
    fun setPullRequestId(id: Int) {
        this.pullRequestId.postValue(id)
    }

    /**
     * Observe this LiveData to get the current refreshing status of comments
     */
    fun isRefreshing() = MediatorLiveData<Boolean>().apply { 
        addSource(refreshTriggered) { resource -> value = resource.resourceStatus == ResourceStatus.LOADING }   
        addSource(pullRequestIdChanged) { resource -> value = resource.resourceStatus == ResourceStatus.LOADING }   
    }

    /**
     * Call this method to refresh the whole pull request
     */
    fun refresh() {
        refreshTrigger.postValue(Unit)  
    } 

    /**
     * All UI events that may happen regarding PR comments.
     */
    sealed class UiEvent {

        /**
         * A comment was sent successfully
         */
        object CommentSendSuccess : UiEvent()

        /**
         * While sending a comment, an error occurred.
         */
        class CommentSendError(val error: String?) : UiEvent()

        /**
         * A comment is currently sent to the server.
         */
        object CommentSendLoading : UiEvent()

        /**
         * A comment was successfully deleted.
         */
        object CommentDeleteSuccess : UiEvent()

        /**
         * While deleting a comment, there was an error.
         */
        class CommentDeleteError(val error: String?) : UiEvent()

        /**
         * A comment is currently deleted.
         */
        object CommentDeleteLoading : UiEvent()
    }
}