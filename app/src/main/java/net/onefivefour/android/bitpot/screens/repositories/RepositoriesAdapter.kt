package net.onefivefour.android.bitpot.screens.repositories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.DataBindingViewHolder
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.Repository

/**
 * This [PagingDataAdapter] is responsible for handling a list of Bitbucket Repositories.
 * Use [submitData] to set a new list of items. They are compared using the provided DiffCallback.
 */
class RepositoriesAdapter(private val clickListener: ItemClickListener) : PagingDataAdapter<Repository, DataBindingViewHolder<Repository>>(RepositoriesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<Repository> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int) = R.layout.list_item_repository
    
    override fun onBindViewHolder(holder: DataBindingViewHolder<Repository>, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, clickListener)
    }

}
