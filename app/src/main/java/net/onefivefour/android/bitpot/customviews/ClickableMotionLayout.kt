package net.onefivefour.android.bitpot.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout


/**
 * MotionLayout will intercept all touch events and take control over them.
 * That means that View on top of MotionLayout (i.e. children of MotionLayout) will not
 * receive touch events.
 *
 * If the motion scene uses only a onSwipe transition, all click events are intercepted nevertheless.
 * This is why we override onInterceptTouchEvent in this class and only let swipe actions be handled
 * by MotionLayout. All other actions are passed down the View tree so that possible ClickListener can
 * receive the touch/click events.
 */
class ClickableMotionLayout : MotionLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            return super.onInterceptTouchEvent(event)
        }
        return false
    }

}