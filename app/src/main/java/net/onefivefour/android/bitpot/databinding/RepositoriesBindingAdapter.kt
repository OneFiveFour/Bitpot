package net.onefivefour.android.bitpot.databinding

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.PipelineState


/**
 * show the correct icon for a [PipelineState]
 */
@BindingAdapter("pipelineState")
fun setPipelineState(imageView: ImageView, pipelineState: PipelineState?) {

    // no pipeline result -> hide imageView
    if (pipelineState == null) {
        imageView.setImageResource(android.R.color.transparent)
        return
    }

    // get correct drawable for pipeline result
    val drawable = getPipelineDrawable(pipelineState)

    val animatedDrawables = listOf(
        R.drawable.ic_pipeline_in_progress_animated,
        R.drawable.ic_pipeline_pending_animated
    )

    // This code is needed to make animated vector-drawables work
    if (animatedDrawables.contains(drawable)) {
        startDrawableAnimation(imageView, drawable)
    } else {
        imageView.setImageResource(drawable)
    }


}

private fun startDrawableAnimation(imageView: ImageView, drawable: Int) {
    val animatedVector = AnimatedVectorDrawableCompat.create(imageView.context, drawable) ?: return
    imageView.setImageDrawable(animatedVector)
    val mainHandler = Handler(Looper.getMainLooper())
    animatedVector.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            mainHandler.post { animatedVector.start() }
        }
    })
    animatedVector.start()
}

private fun getPipelineDrawable(pipelineState: PipelineState): Int {
    return when (pipelineState) {
        PipelineState.PENDING -> R.drawable.ic_pipeline_pending_animated
        PipelineState.IN_PROGRESS -> R.drawable.ic_pipeline_in_progress_animated
        PipelineState.PAUSED -> R.drawable.ic_pipeline_paused
        PipelineState.SUCCESSFUL -> R.drawable.ic_check_mark
        PipelineState.STOPPED -> R.drawable.ic_pipeline_stopped
        PipelineState.EXPIRED,
        PipelineState.FAILED,
        PipelineState.ERROR -> R.drawable.ic_pipeline_failed
        PipelineState.UNKNOWN -> 0
    }
}