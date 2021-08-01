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
import net.onefivefour.android.bitpot.data.model.Source

/**
 * The Paging adapter for [Source]s.
 * 
 * This adapter is using the Jetpack Paging library 3.0 and works together with [androidx.paging.PagingData]
 *  Use [submitData] to set a new list of items. They are compared using the provided DiffCallback.
 */
class SourcesAdapter(private val clickListener: ItemClickListener) : PagingDataAdapter<Source, DataBindingViewHolder<Source>>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Source> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<Source>, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, clickListener)
    }

    override fun getItemViewType(position: Int) = R.layout.list_item_source

    /**
     * This callback is used to calculate the difference of to lists of Sources.
     * Be as specific as possible when comparing the contents to avoid strange behavior.
     */
    class DiffCallback : DiffUtil.ItemCallback<Source>() {
        override fun areItemsTheSame(oldItem: Source, newItem: Source): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Source, newItem: Source): Boolean {
            return oldItem.path == newItem.path &&
                    oldItem.size == newItem.size &&
                    oldItem.type == newItem.type
        }
    }
}
