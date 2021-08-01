package net.onefivefour.android.bitpot.screens.sources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.DataBindingViewHolder
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.Ref

/**
 * This adapter is responsible for handling a list of Bitbucket Refs (i.e. branches and tags).
 * Use [submitData] to set a new list of items. They are compared using the provided DiffCallback.
 *
 * Since this is a [PagingDataAdapter], the list elements are considered immutable! Therefore we cannot submit new items arbitrarily.
 * In other words: "submitData" should only be called once when setting the initial data for this adapter. All future updates on the list items must
 * come via the paging library.
 * 
 * See the Jetpack Paging library version 3.0 for details on a [PagingDataAdapter]
 */
class RefsAdapter(private val clickListener: ItemClickListener) : PagingDataAdapter<Ref, DataBindingViewHolder<Ref>>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Ref> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<Ref>, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, clickListener)
    }

    override fun getItemViewType(position: Int) = R.layout.list_item_ref_type

    /**
     * This callback is used to calculate the difference of to lists of Refs.
     * Be as specific as possible when comparing the contents to avoid strange behavior.
     */
    class DiffCallback : DiffUtil.ItemCallback<Ref>() {
        override fun areItemsTheSame(oldItem: Ref, newItem: Ref): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ref, newItem: Ref): Boolean {
            return oldItem.name == newItem.name
        }
    }
}
