package net.onefivefour.android.bitpot.screens.pullrequest

import androidx.recyclerview.widget.DiffUtil
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.DataBindingAdapter
import net.onefivefour.android.bitpot.common.DataBindingViewHolder
import net.onefivefour.android.bitpot.customviews.pullrequest.CommentView
import net.onefivefour.android.bitpot.customviews.pullrequest.DiffView
import net.onefivefour.android.bitpot.data.model.Comment
import net.onefivefour.android.bitpot.data.model.DiffItem
import net.onefivefour.android.bitpot.data.model.FileDiff

/**
 * An RecyclerView adapter to display a [DiffItem] item.
 *
 * A [DiffItem] is currently either a [FileDiff] or a [Comment].
 * Both model classes can be part of the pull request RecyclerView.
 *
 * @param commentCallback this callback is used to handle clicks of a [DiffView].
 * @param commentViewClickListener this callback is used to handle clicks of a [CommentView].
 */
class DiffAdapter(private val commentCallback: DiffView.CommentCallback, private val commentViewClickListener: CommentView.ClickListener) : DataBindingAdapter<DiffItem>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is FileDiff -> R.layout.list_item_diff
            is Comment -> R.layout.list_item_comment
            else -> throw IllegalArgumentException("Cannot get unknown ItemViewType in DiffAdapter ${item::class.java}")
        }
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<DiffItem>, position: Int) {
        when (val item = getItem(position)) {
            is FileDiff ->  holder.bind(item, commentCallback)
            is Comment -> holder.bind(item, commentViewClickListener)
            else -> throw IllegalArgumentException("Cannot bind unknown ItemViewType in DiffAdapter ${item::class.java}")
        }
    }

    /**
     * This callback is used to calculate the difference of to lists of [DiffItem]s.
     * Be as specific as possible when comparing the contents to avoid strange behavior.
     *
     * [DiffItem] is currently implemented by [FileDiff] and [Comment], so we have to check both cases here.
     */
    class DiffCallback : DiffUtil.ItemCallback<DiffItem>() {

        override fun areItemsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean {
            if (oldItem::class.java != newItem::class.java) return false

            return when (oldItem) {
                is Comment -> compareCommentItem(oldItem, newItem as Comment)
                is FileDiff -> compareFileDiffItem(oldItem, newItem as FileDiff)
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean {
            if (oldItem::class.java != newItem::class.java) return false

            return when (oldItem) {
                is Comment -> compareCommentContent(oldItem, newItem as Comment)
                is FileDiff -> compareFileDiffContent(oldItem, newItem as FileDiff)
                else -> false
            }
        }

        private fun compareCommentItem(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        private fun compareFileDiffItem(oldItem: FileDiff, newItem: FileDiff) : Boolean {
            return oldItem.filePath == newItem.filePath
        }

        private fun compareCommentContent(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.updatedOn == newItem.updatedOn &&
                    oldItem.content == newItem.content &&
                    oldItem.author.accountId == newItem.author.accountId
        }

        private fun compareFileDiffContent(oldItem: FileDiff, newItem: FileDiff): Boolean {
            return oldItem.filePath == newItem.filePath &&
                    oldItem.hasMergeConflict == newItem.hasMergeConflict &&
                    oldItem.type == newItem.type &&
                    oldItem.longestLine == newItem.longestLine &&
                    oldItem.code == newItem.code &&

                    // compare comments
                    areCommentsTheSame(oldItem.comments, newItem.comments)
        }

        private fun areCommentsTheSame(oldComments: List<Comment>, newComments: List<Comment>): Boolean {
            if (oldComments.size != newComments.size) return false

            oldComments.forEachIndexed { index, oldComment ->
                val newComment = newComments[index]
                if (oldComment.id != newComment.id || oldComment.updatedOn != newComment.updatedOn) {
                    return false
                }
            }

            return true
        }
    }
}
