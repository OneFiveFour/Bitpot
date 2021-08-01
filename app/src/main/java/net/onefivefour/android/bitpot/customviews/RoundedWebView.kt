package net.onefivefour.android.bitpot.customviews

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.webkit.WebView
import net.onefivefour.android.bitpot.extensions.dpToPx

/**
 * This class is a WebView with rounded corners.
 * The corners are calculated in a way that allows it to cast shadows via app:elevation
 * Each corner has a fixed radius of 10dp.
 */
class RoundedWebView : WebView {

    companion object {
        const val CORNER_RADIUS = 10f
    }

    init {
        clipToOutline = true
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        outlineProvider = getShadowProvider()
    }

    private fun getShadowProvider() = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, CORNER_RADIUS.dpToPx(context))
        }
    }
}