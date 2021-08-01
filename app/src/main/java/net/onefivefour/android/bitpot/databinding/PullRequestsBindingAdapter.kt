package net.onefivefour.android.bitpot.databinding

import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import io.noties.markwon.Markwon
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.customviews.participants.ParticipantsListLayout
import net.onefivefour.android.bitpot.data.model.Participant
import net.onefivefour.android.bitpot.data.model.PullRequest
import net.onefivefour.android.bitpot.screens.pullrequest.PullRequestViewModel


/**
 * Set an [ItemClickListener] for [PullRequest] to a view.
 */
@BindingAdapter("pullRequestClick", "pullRequest")
fun setPullRequestClick(view: View, clickListener: ItemClickListener?, pullRequest: PullRequest?) {
    if (clickListener == null) return
    if (pullRequest == null) return
    view.setOnClickListener {
        clickListener.onClick(view, pullRequest)
    }
}

/**
 * Sets the title of a pull request. It is the source branch name and the destination branch name
 * connected with an rounded right arrow.
 */
@BindingAdapter("pullRequestTitle")
fun setPullRequestTitle(textView: TextView, pullRequest: PullRequest?) {
    if (pullRequest == null) return
    textView.text = ellipsizePullRequestTitle(pullRequest.sourceBranchName, pullRequest.destinationBranchName)
}

private fun ellipsizePullRequestTitle(sourceBranchName: String, destinationBranchName: String): String {
    val maxLength = 15

    // ellipsize source branch if longer than maxLength chars
    val source = when {
        sourceBranchName.length > maxLength -> "${sourceBranchName.take(maxLength)}\u2026"
        else -> sourceBranchName
    }

    // ellipsize destination branch if longer than maxLength chars
    val destination = when {
        destinationBranchName.length > maxLength -> "${destinationBranchName.take(maxLength)}\u2026"
        else -> destinationBranchName
    }

    // concat branches using rounded arrow ascii
    return "$source \u2794 $destination"
}


@Suppress("ClickableViewAccessibility")
@BindingAdapter("pullRequestDescription")
fun setPullRequestDescription(textView: TextView, markdown: String?) {
    if (markdown == null) return

    val markwon = Markwon.create(textView.context)
    markwon.setMarkdown(textView, markdown)

    val endY = textView.lineHeight * textView.lineCount - textView.lineHeight * textView.maxLines
    if (endY <= 0) return // all is visible, no animation needed

    val animator = ObjectAnimator.ofInt(textView, "scrollY", 0, endY / 3, endY * 2 / 3, endY, endY).apply {
        duration = 800L * textView.lineCount
        interpolator = LinearInterpolator()
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.RESTART
        startDelay = 2000L
        start()
    }

    textView.setOnTouchListener { _, event ->
        val isActionDown = event.action == MotionEvent.ACTION_DOWN
        if (isActionDown) animator.cancel()
        textView.parent.requestDisallowInterceptTouchEvent(isActionDown)
        return@setOnTouchListener false
    }
}


@BindingAdapter("participants", "borderColor")
fun setParticipants(participantsListLayout: ParticipantsListLayout, participants: List<Participant>?, @ColorInt borderColor: Int?) {
    if (participants.isNullOrEmpty()) return
    if (borderColor == null) return
    participantsListLayout.setParticipants(participants, borderColor)
}


@BindingAdapter("hasApproved", "viewModel")
fun hasApproved(button: MaterialButton, hasApproved: Boolean?, viewModel: PullRequestViewModel?) {
    if (hasApproved == null) return
    if (viewModel == null) return
    val stringResId = if (hasApproved) R.string.unapprove else R.string.approve
    button.text = button.context.getString(stringResId)
    val drawableRes = when {
        hasApproved -> R.drawable.ic_cross
        else -> R.drawable.ic_check_mark_transparent
    }
    button.icon = ContextCompat.getDrawable(button.context, drawableRes)
    button.setOnClickListener { viewModel.setApproval(!hasApproved) }
}

@BindingAdapter("isCommentsRefreshing", "isDiffRefreshing")
fun setPullRequestRefreshing(swipeRefreshLayout: SwipeRefreshLayout, isCommentsRefreshing: Boolean?, isDiffRefreshing: Boolean?) {
    swipeRefreshLayout.isRefreshing = isCommentsRefreshing ?: false || isDiffRefreshing ?: false
}