package net.onefivefour.android.bitpot.screens.pipelines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.DataBindingViewHolder
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.Pipeline

/**
 * The Paging adapter for [Pipeline]s.
 *
 * This adapter is using the Jetpack Paging library 3.0 and works together with [androidx.paging.PagingData]
 *  Use [submitData] to set a new list of items. They are compared using the provided DiffCallback.
 */
class PipelinesAdapter(private val clickListener: ItemClickListener) : PagingDataAdapter<Pipeline, DataBindingViewHolder<Pipeline>>(PipelinesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Pipeline> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<Pipeline>, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, clickListener)
    }

    override fun getItemViewType(position: Int) =  R.layout.list_item_pipeline
}
