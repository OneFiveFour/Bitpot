package net.onefivefour.android.bitpot.databinding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.Ref
import net.onefivefour.android.bitpot.data.model.RefType

/**
 * Set an [ItemClickListener] for a [Ref] to a view.
 */
@BindingAdapter("refClick", "ref")
fun setRefClick(view: View, clickListener: ItemClickListener, ref: Ref) {
    view.setOnClickListener {
        clickListener.onClick(view, ref)
    }
}

@BindingAdapter("refDrawable")
fun setRefDrawable(textView: TextView, refType: RefType) {
    val drawable = when (refType) {
        RefType.BRANCH -> R.drawable.ic_branch
        RefType.TAG -> R.drawable.ic_tag
        RefType.ALL -> 0
    }

    textView.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}