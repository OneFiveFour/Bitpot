package net.onefivefour.android.bitpot.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.onefivefour.android.bitpot.R

/**
 * An [RecyclerView.ItemDecoration] to put space between its items.
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val resources = view.context.resources
        val horizontalSpace = resources.getDimensionPixelSize(R.dimen.default_list_item_margin_horizontal)
        val verticalSpace = resources.getDimensionPixelSize(R.dimen.default_list_item_margin_vertical)

        outRect.left = horizontalSpace
        outRect.right = horizontalSpace
        outRect.bottom = verticalSpace
        outRect.top = verticalSpace
    }
}