package net.onefivefour.android.bitpot.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import net.onefivefour.android.bitpot.BR
import net.onefivefour.android.bitpot.customviews.pullrequest.CommentView
import net.onefivefour.android.bitpot.customviews.pullrequest.DiffView
import net.onefivefour.android.bitpot.data.model.Download


/**
 * A generic ViewHolder that is used in connection with DataBindingAdapter.
 * The passed item and the clickListener are automatically mapped to the data-binding variable called "item".
 * So make sure that you use this name in your xml layout if you want to make use of this ViewHolder.
 */
class DataBindingViewHolder<T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    var item: T? = null
        private set

    fun bind(item: T, commentCallback: DiffView.CommentCallback) {
        this.item = item
        binding.setVariable(BR.item, item)
        binding.setVariable(BR.commentCallback, commentCallback)
    }

    fun bind(item: T, clickListener: CommentView.ClickListener) {
        this.item = item
        binding.setVariable(BR.item, item)
        binding.setVariable(BR.clickListener, clickListener)
    }

    fun bind(item: T, clickListener: ItemClickListener) {
        this.item = item
        binding.setVariable(BR.item, item)
        binding.setVariable(BR.clickListener, clickListener)
        binding.executePendingBindings()
    }

    fun bind(item: T) {
        this.item = item
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }

    /**
     * use this to update the progress of a [Download]
     * if the list item is null or not of type [Download], this call will do nothing.
     */
    fun bindProgress(progress: Float) {
        item?.let { download ->
            if (download !is Download) return
            download.downloadProgress = progress
            binding.invalidateAll()
        }
    }
}