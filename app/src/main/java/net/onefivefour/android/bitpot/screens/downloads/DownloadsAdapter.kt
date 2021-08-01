package net.onefivefour.android.bitpot.screens.downloads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.DataBindingViewHolder
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.Download


/**
 * The Paging adapter for [Download]s.
 *
 * This adapter is using the Jetpack Paging library 3.0 and works together with [androidx.paging.PagingData]
 *  Use [submitData] to set a new list of items. They are compared using the provided DiffCallback.
 */
class DownloadsAdapter(private val clickListener: ItemClickListener) : PagingDataAdapter<Download, DataBindingViewHolder<Download>>(DownloadsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Download> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int) = R.layout.list_item_download

    override fun onBindViewHolder(holder: DataBindingViewHolder<Download>, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, clickListener)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<Download>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bindProgress(payloads[0] as Float)
        }
    }

}
