package net.onefivefour.android.bitpot.screens.pullrequest

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.navArgs
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.DiffItemDecoration
import net.onefivefour.android.bitpot.customviews.pullrequest.CommentView
import net.onefivefour.android.bitpot.customviews.pullrequest.DiffView
import net.onefivefour.android.bitpot.data.model.CommentPosition
import net.onefivefour.android.bitpot.databinding.ActivityPullRequestBinding
import net.onefivefour.android.bitpot.screens.repository.RepositoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


/**
 * This Activity displays details of a pull request.
 * The user can approve and merge a pull request here.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PullRequestActivity : AppCompatActivity(), DiffView.CommentCallback, CommentView.ClickListener {
    
    private lateinit var binding: ActivityPullRequestBinding

    private val args: PullRequestActivityArgs by navArgs()

    private val repositoryViewModel: RepositoryViewModel by viewModel { parametersOf(args.workspaceUuid, args.repositoryUuid) }
    
    private val pullRequestViewModel: PullRequestViewModel by viewModel()
    
    private val diffViewModel: DiffViewModel by viewModel()
    
    private val commentViewModel: CommentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)        

        binding = ActivityPullRequestBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
            it.pullRequestId = args.pullRequestId
            it.pullRequestViewModel = pullRequestViewModel
            it.repositoryViewModel = repositoryViewModel
            it.diffViewModel = diffViewModel
            it.commentViewModel = commentViewModel
        }
        
        setContentView(binding.root)

        setupUi()

        observeViewModels()
    }

    private fun setupUi() {
        ViewCompat.setElevation(binding.include.root, resources.getDimension(R.dimen.dp_8))

        binding.rvDiff.adapter = DiffAdapter(this, this)
        binding.rvDiff.addItemDecoration(DiffItemDecoration())

        binding.strDiff.setOnRefreshListener {
            diffViewModel.refresh()
            commentViewModel.refresh()
        }

        binding.btnAddComment.setOnClickListener { showCommentDialog(null, CommentPosition.PullRequest) }
        binding.btnMergePullRequest.setOnClickListener { showMergeDialog() }
    }

    private fun observeViewModels() {
        repositoryViewModel.repository.observe(this) { repository -> 
            repository ?: return@observe
            commentViewModel.setPullRequestId(args.pullRequestId)
            pullRequestViewModel.setPullRequestId(args.pullRequestId)
            diffViewModel.setPullRequestId(args.pullRequestId)
        }

        commentViewModel.getComments().observe(this) { comments ->
            comments ?: return@observe
            diffViewModel.setComments(comments)
        }
        
        diffViewModel.getDiff().observe(this) { diffList ->
            diffList ?: return@observe
            (binding.rvDiff.adapter as DiffAdapter).submitList(diffList)
        }

        commentViewModel.getCommentUiEvents().observe(this) { event ->
            when (event) {
                CommentViewModel.UiEvent.CommentSendSuccess -> showSnackBar(stringResId = R.string.comment_was_sent_successfully)
                is CommentViewModel.UiEvent.CommentSendError -> showSnackBar(event.error, R.string.error_while_sending_comment)
                CommentViewModel.UiEvent.CommentDeleteSuccess -> showSnackBar(stringResId = R.string.comment_was_deleted_successfully)
                is CommentViewModel.UiEvent.CommentDeleteError -> showSnackBar(event.error, R.string.error_while_deleting_comment)
                CommentViewModel.UiEvent.CommentDeleteLoading,
                CommentViewModel.UiEvent.CommentSendLoading -> { /* do nothing */ } 
            }
        }

        pullRequestViewModel.getPullRequestUiEvents().observe(this) { event ->
            when (event) {
                is PullRequestViewModel.UiEvent.MergeSuccess -> showSnackBar(stringResId = R.string.merge_successful)
                is PullRequestViewModel.UiEvent.MergeError -> showSnackBar(event.error, R.string.error_while_merging)
                PullRequestViewModel.UiEvent.MergeLoading -> { /* do nothing */ }
            }
        }
    }

    override fun onReplyClicked(clickedCommentId: Int, commentPosition: CommentPosition) {
        showCommentDialog(clickedCommentId, commentPosition)
    }

    override fun onCreateComment(parentId: Int?, commentPosition: CommentPosition) {
        showCommentDialog(parentId, commentPosition)
    }

    override fun onDeleteClicked(clickedCommentId: Int) {
        commentViewModel.deleteComment(clickedCommentId)
    }

    override fun onDeleteComment(clickedCommentId: Int) {
        commentViewModel.deleteComment(clickedCommentId)
    }

    private fun showCommentDialog(parentId: Int?, commentPosition: CommentPosition) {
        CommentDialog(this, parentId, commentPosition) { newComment ->
            when {
                newComment.content.isEmpty() -> showSnackBar(stringResId = R.string.no_comment_sent_because_empty)
                else -> commentViewModel.createComment(newComment)
            }
        }.show()
    }

    private fun showMergeDialog() {
        MergeDialog(this) { merge ->
            pullRequestViewModel.merge(merge)
        }.show()
    }

    private fun showSnackBar(stringMessage: String? = null, @StringRes stringResId: Int) {
        val message = if (stringMessage.isNullOrEmpty()) getString(stringResId) else stringMessage
        val snackBar = Snackbar.make(binding.mlRootPullRequestActivity, message, Snackbar.LENGTH_SHORT)
        val marginBottom = resources.getDimension(R.dimen.dp_48)
        snackBar.view.translationY = -marginBottom
        snackBar.show()
    }
}