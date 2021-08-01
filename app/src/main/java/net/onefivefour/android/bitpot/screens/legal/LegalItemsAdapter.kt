package net.onefivefour.android.bitpot.screens.legal

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.DataBindingAdapter
import net.onefivefour.android.bitpot.common.DataBindingViewHolder
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.LegalItem

/**
 * This ListAdapter is responsible for handling a list of [LegalItem]s.
 * Use [submitList] to set a new list of items. They are compared using the provided DiffCallback.
 *
 * Since this is a [PagingDataAdapter], the list elements are considered immutable! Therefore we cannot submit new items arbitrarily.
 * Instead, all list updates must come from the connected [androidx.paging.DataSource].
 * In other words: "submitList" should only be called once when setting the initial data for this adapter. All future updates on the list items must
 * come via the paging library.
 */
class LegalItemsAdapter(private val clickListener: ItemClickListener) : DataBindingAdapter<LegalItem>(DiffCallback()) {

    override fun onBindViewHolder(holder: DataBindingViewHolder<LegalItem>, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, clickListener)
    }

    override fun getItemViewType(position: Int) = R.layout.list_item_legal

    /**
     * This callback is used to calculate the difference of to lists of LegalItems.
     * Be as specific as possible when comparing the contents to avoid strange behavior.
     */
    class DiffCallback : DiffUtil.ItemCallback<LegalItem>() {
        override fun areItemsTheSame(oldItem: LegalItem, newItem: LegalItem): Boolean {
            return oldItem.titleRes == newItem.titleRes
        }

        override fun areContentsTheSame(oldItem: LegalItem, newItem: LegalItem): Boolean {
            return oldItem.titleRes == newItem.titleRes &&
                    oldItem.lastUpdated == newItem.lastUpdated
        }
    }
}
