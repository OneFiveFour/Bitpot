package net.onefivefour.android.bitpot.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.customviews.pullrequest.DiffView

/**
 * An [RecyclerView.ItemDecoration] to put space between its items.
 */
class DiffItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val space = view.context.resources.getDimensionPixelSize(R.dimen.dp_16)
        val position = parent.getChildAdapterPosition(view)

        if (view is DiffView && position != 0) outRect.top = space

        val lastItemPosition = (parent.adapter?.itemCount ?: 0) - 1
        if (position == lastItemPosition) outRect.bottom = space

    }
}