package net.onefivefour.android.bitpot.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * This generic Adapter can be used to use any data class T as data provider for recyclerView list items.
 * To send a list of items to this adapter, use the submitList(newList) method. The DiffCallback is then called to
 * detect which items are completely new or have changed content. Animations are created accordingly and automatically.
 */
abstract class DataBindingAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>) : ListAdapter<T, DataBindingViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}