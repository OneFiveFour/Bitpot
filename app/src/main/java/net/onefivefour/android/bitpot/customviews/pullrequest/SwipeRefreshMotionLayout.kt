package net.onefivefour.android.bitpot.customviews.pullrequest

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


/**
 * The current version of motionLayout (2.0.0-beta04) does not honor the position
 * of the RecyclerView, if it is wrapped in a SwipeRefreshLayout.
 * This is the case for the PullRequest screen: When scrolling back to top, the motionLayout transition
 * would be triggered immediately instead of only as soon as the RecyclerView scrolled back to top.
 *
 * This workaround checks if the SwipeRefresh layout can still scroll back up. If so, it does not trigger the motionLayout transition.
 */
class SwipeRefreshMotionLayout : MotionLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (target !is SwipeRefreshLayout) {
            return super.onNestedPreScroll(target, dx, dy, consumed, type)
        }

        val recyclerView = target.getChildAt(0)
        if (recyclerView !is RecyclerView) {
            return super.onNestedPreScroll(target, dx, dy, consumed, type)
        }

        val canScrollVertically = recyclerView.canScrollVertically(-1)
        if (dy < 0 && canScrollVertically) {
            // don't start motionLayout transition
            return
        }

        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }
}