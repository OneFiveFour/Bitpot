package net.onefivefour.android.bitpot.extensions

import android.graphics.drawable.GradientDrawable
import android.view.View
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.RepositoryColors
import net.onefivefour.android.bitpot.model.RoundEdgePosition

fun View.fadeOut() {
    this.animate().alpha(0f).start()
}
fun View.fadeIn() {
    this.animate().alpha(1f).start()
}

/**
 * The given View is filled with a horizontal gradient background and rounded corners.
 */
fun View.setGradient(colors: RepositoryColors, roundEdgePosition: RoundEdgePosition = RoundEdgePosition.BOTTOM) {
    val cornerRadius = context.resources.getDimension(R.dimen.bottom_nav_corner_radius)

    val gradientColors = intArrayOf(colors.gradientFromColor, colors.gradientToColor)
    background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors).apply {
        cornerRadii = when (roundEdgePosition) {
            RoundEdgePosition.TOP -> floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f, 0f, 0f)
            RoundEdgePosition.BOTTOM -> floatArrayOf(0f, 0f, 0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
        }
    }
}