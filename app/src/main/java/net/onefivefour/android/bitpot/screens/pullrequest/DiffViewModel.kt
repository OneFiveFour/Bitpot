package net.onefivefour.android.bitpot.screens.pullrequest

import androidx.lifecycle.*
import net.onefivefour.android.bitpot.data.extensions.sort
import net.onefivefour.android.bitpot.data.model.*
import net.onefivefour.android.bitpot.data.repositories.DiffRepository

/**
 * The ViewModel that should be used to display a git diff.
 */
class DiffViewModel : ViewModel() {

    /**
     * Used to trigger a complete refresh of this diff
     */
    private val refreshTrigger = MutableLiveData<Unit>()

    /**
     * The current pull request id
     */
    private val pullRequestId = MutableLiveData<Int>()

    /**
     * The list of comments of the current pull request
     */
    private val comments = MutableLiveData<List<Comment>>()

    /**
     * This LiveData loads the diff for every new pull request id
     */
    private val pullRequestIdChanged = Transformations.switchMap(pullRequestId) { id ->
        DiffRepository.getDiff(id)
    }

    /**
     * This LiveData refreshes the diff of the current pull request
     */
    private val refreshTriggered = Transformations.switchMap(refreshTrigger) {
        val id = pullRequestId.value ?: return@switchMap null
        DiffRepository.getDiff(id)
    }

    /**
     * Whenever a new diff api call was made, this LiveData will check for a successful call and
     * expose the resulting list of [FileDiff]
     */
    private val diffList = MediatorLiveData<List<FileDiff>>().apply {
        addSource(pullRequestIdChanged) { resource ->
            value = when (resource?.resourceStatus) {
                ResourceStatus.SUCCESS -> resource.data ?: emptyList()
                else -> emptyList()
            }
        }
        addSource(refreshTriggered) { resource ->
            value = when (resource?.resourceStatus) {
                ResourceStatus.SUCCESS -> resource.data ?: emptyList()
                else -> emptyList()
            }
        }
    }

    /**
     * Observe this LiveData to get the diff content of the whole pull request.
     *
     * @return LiveData with a list of [DiffItem]s.
     * These items consist of comments to the PR as well as the list of changed files with their respective comments.
     */
    fun getDiff() = MediatorLiveData<List<DiffItem>>().apply {
        addSource(diffList) { fileDiffs ->
            if (fileDiffs == null) return@addSource
            val commentList = comments.value?.toList() ?: emptyList()
            value = mergeDiffAndComments(fileDiffs, commentList)
        }

        addSource(comments) { commentsList ->
            if (commentsList == null) return@addSource
            val fileDiffs = diffList.value ?: return@addSource
            value = mergeDiffAndComments(fileDiffs, commentsList)
        }
    }

    /**
     * Observe this LiveData to get updated about the loading state of this diff.
     */
    val isLoading: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(pullRequestIdChanged) { value = it?.resourceStatus == ResourceStatus.LOADING }
        addSource(refreshTriggered) { value = it?.resourceStatus == ResourceStatus.LOADING }
    }

    /**
     * This method takes a list of FileDiffs and a list of Comments.
     * It formats the content of these lists in a way that is ready to be displayed in the UI:
     *
     * * First all PR comments
     * * Then all PR files including their respective comments
     *
     * @return a List<DiffItem> that is ready to be shown in a RecyclerView.
     */
    private fun mergeDiffAndComments(files: List<FileDiff>, comments: List<Comment>): List<DiffItem> {
        val result = arrayListOf<DiffItem>()

        // First get all PR comments.
        val pullRequestComments = comments.filter { comment -> comment.position is CommentPosition.PullRequest }.sort()

        // Then merge all comments with their respective files.
        val filesToComments = mapFilesToComments(files, comments)
        val pullRequestFiles = compileFiles(filesToComments)

        // At last add everything in the right order
        result.addAll(pullRequestComments)
        result.addAll(pullRequestFiles)

        return result
    }

    /**
     * @param fileToComments a map that has maps from a FileDiff to its comments.
     *
     * @return a list of FileDiffs where each file has its comments correctly setup
     */
    private fun compileFiles(fileToComments: Map<FileDiff, List<Comment>>): List<FileDiff> {
        return fileToComments.map fileCommentMap@{ (file, comments) ->

            // immediately return files without comments
            if (comments.isEmpty()) return@fileCommentMap file

            // We have to create a copy of the file, because:
            // If the Diff RecyclerView already displays this file along with its comments,
            // it holds a reference on both the file and the comments. If we now get new comments
            // on the file and would just update them without creating a new file instance, that step
            // would also update the list reference that we already show in the RecyclerView.
            // Therefore the DiffUtil would not recognize any change and ignore the new comment.
            // So we always have to create a new instance of each file that has comments in it.
            val fileDiffCopy = file.copy(
                file.filePath,
                file.longestLine,
                emptyList(),
                emptyList(),
                file.type,
                file.hasMergeConflict,
                file.lineNumberStringLength
            )

            // first set a list of all file comments
            fileDiffCopy.comments = comments.filter { comment -> comment.position is CommentPosition.File }.sort()

            // then iterate over each line of code to check for comments on these lines.
            val allLineComments = comments.filter { comment -> comment.position is CommentPosition.Line }
            fileDiffCopy.code = file.code.map lineMap@{ line ->

                // get comments for this line
                val lineComments = allLineComments.filter { comment ->
                    val commentSourceLine = (comment.position as CommentPosition.Line).sourceLine
                    val commentDestinationLine = (comment.position as CommentPosition.Line).destinationLine

                    when (commentSourceLine) {
                        -1 -> line.destinationLineNumber == commentDestinationLine
                        else -> line.sourceLineNumber == commentSourceLine
                    }
                }.sort()

                // immediately return line without comments
                if (lineComments.isEmpty()) return@lineMap line

                // create a copy of this line for the same reason as described for "fileDiffCopy" above
                val lineCopy = line.copy(
                    line.type,
                    line.sourceLineNumber,
                    line.destinationLineNumber,
                    line.content,
                    lineComments
                )

                lineCopy
            }

            fileDiffCopy
        }
    }

    /**
     * This method creates the map used for compileFiles().
     * The map is mapping from a FileDiff to its comments.
     * If a FileDiff has no comments, it is still part of the returned value.
     */
    private fun mapFilesToComments(files: List<FileDiff>, comments: List<Comment>): Map<FileDiff, List<Comment>> {
        return files.associateWith { file ->
            comments.filter { comment ->
                val filePathToFilter = file.filePath.toString()
                val filePathOfComment = when (val position = comment.position) {
                    is CommentPosition.File -> position.filePath
                    is CommentPosition.Line -> position.filePath
                    else -> return@filter false // only line and file comments are relevant
                }
                filePathToFilter == filePathOfComment
            }
        }
    }

    /**
     * Observe this LiveData to get notified about the refresh state of the Diff
     */
    fun isRefreshing() = Transformations.map(refreshTriggered) { resource ->
        resource.resourceStatus == ResourceStatus.LOADING
    }

    /**
     * Call this method to refresh the whole diff
     */
    fun refresh() = refreshTrigger.postValue(Unit)

    /**
     * Call this method to set a new PullRequestId
     */
    fun setPullRequestId(id: Int) = pullRequestId.postValue(id)

    /**
     * Set the list of comments of the current PullRequest.
     */
    fun setComments(comments: List<Comment>) {
        this.comments.postValue(comments)
    }
}