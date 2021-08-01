package net.onefivefour.android.bitpot.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import net.onefivefour.android.bitpot.R

/**
 * This ImageView takes two Animated Vector Drawables via xml:
 * app:enabledState and app:disabledState.
 *
 * It provides methods to change between the two drawables by starting
 * their animation.
 */
class AnimatedToggleImageView : AppCompatImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs, defStyleAttr)
    }

    private var enabledState: AnimatedVectorDrawableCompat? = null
    private var disabledState: AnimatedVectorDrawableCompat? = null

    private var isShowingEnabledState: Boolean = true

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int = 0) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedToggleImageView, defStyleAttr, 0)

        val enabledStateResId = typedArray.getResourceId(R.styleable.AnimatedToggleImageView_enabledState, -1)
        val disabledStateResId = typedArray.getResourceId(R.styleable.AnimatedToggleImageView_disabledState, -1)

        if (enabledStateResId == -1 || disabledStateResId == -1) {
            throw IllegalStateException("Both stateOne and stateTwo must be set in layout xml.")
        }

        enabledState = AnimatedVectorDrawableCompat.create(context, enabledStateResId)
        disabledState = AnimatedVectorDrawableCompat.create(context, disabledStateResId)

        enable()

        typedArray.recycle()
    }

    /**
     * Call to animate to the enabled state
     */
    fun enable() {
        setImageDrawable(enabledState)
        enabledState?.start()
        isShowingEnabledState = true
    }

    /**
     * Call to animate to the disabled state
     */
    fun disable() {
        setImageDrawable(disabledState)
        disabledState?.start()
        isShowingEnabledState = false
    }

    /**
     * Call to toggle to the other state.
     */
    fun toggle() {
        val drawable = if (isShowingEnabledState) disabledState else enabledState
        setImageDrawable(drawable)
        drawable?.start()
        isShowingEnabledState = !isShowingEnabledState
    }
}