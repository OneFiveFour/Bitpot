package net.onefivefour.android.bitpot.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import net.onefivefour.android.bitpot.data.model.LegalItem
import net.onefivefour.android.bitpot.screens.legal.LegalItemsAdapter

/**
 * This BindingAdapter is used to fill the RecyclerView with the
 * hard-coded legal items.
 */
@BindingAdapter("legalItems")
fun setLegalItems(recyclerView: RecyclerView, items: List<LegalItem>?) {
    if (items == null) return
    (recyclerView.adapter as LegalItemsAdapter).submitList(items)
}