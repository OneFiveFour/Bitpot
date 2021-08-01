package net.onefivefour.android.bitpot.screens.pullrequests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.DataBindingViewHolder
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.PullRequest

/**
 * The Paging adapter for [PullRequest]s.
 *
 * This adapter is using the Jetpack Paging library 3.0 and works together with [androidx.paging.PagingData]
 *  Use [submitData] to set a new list of items. They are compared using the provided DiffCallback.
 */
class PullRequestsAdapter(private val clickListener: ItemClickListener) : PagingDataAdapter<PullRequest, DataBindingViewHolder<PullRequest>>(PullRequestsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<PullRequest> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int) = R.layout.list_item_pull_request

    override fun onBindViewHolder(holder: DataBindingViewHolder<PullRequest>, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, clickListener)
    }
}
