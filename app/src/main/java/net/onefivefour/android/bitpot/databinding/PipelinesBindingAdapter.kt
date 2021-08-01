package net.onefivefour.android.bitpot.databinding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.Pipeline
import net.onefivefour.android.bitpot.data.model.PipelineTarget

/**
 * Set an [ItemClickListener] for [Pipeline] to a view.
 */
@BindingAdapter("pipelineClick", "pipeline")
fun setPipelineClick(view: View, clickListener: ItemClickListener, pipeline: Pipeline) {
    view.setOnClickListener {
        clickListener.onClick(view, pipeline)
    }
}

@BindingAdapter("pipelineTarget")
fun setPipelineTarget(textView: TextView, target: PipelineTarget) {
    textView.text = when (target) {
        is PipelineTarget.Commit -> target.commitMessage
        is PipelineTarget.Branch -> target.branchName
        is PipelineTarget.PullRequest -> ellipsizePullRequestString(target)
        PipelineTarget.Unknown -> "<N/A>"
    }
}


private fun ellipsizePullRequestString(target: PipelineTarget.PullRequest): String {
    val maxLength =  15

    // ellipsize source branch if longer than maxLength chars
    val source = when {
        target.source.length > maxLength -> "${target.source.take(maxLength)}\u2026"
        else -> target.source
    }

    // ellipsize destination branch if longer than maxLength chars
    val destination = when {
        target.destination.length > maxLength -> "${target.destination.take(maxLength)}\u2026"
        else -> target.destination
    }

    // concat branches using rounded arrow ascii
    return "$source \u2794 $destination"
}