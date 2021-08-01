package net.onefivefour.android.bitpot.screens.downloads

import androidx.recyclerview.widget.DiffUtil
import net.onefivefour.android.bitpot.data.model.Download

/**
 * This callback is used to calculate the difference of to lists of [Download]s.
 * Be as specific as possible when comparing the contents to avoid strange behavior.
 */
class DownloadsDiffCallback : DiffUtil.ItemCallback<Download>() {
    
    override fun areItemsTheSame(oldItem: Download, newItem: Download): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Download, newItem: Download): Boolean {
        return areDownloadsTheSame(oldItem, newItem) 
    }

    private fun areDownloadsTheSame(oldItem: Download, newItem: Download): Boolean {
        return oldItem.createdOn == newItem.createdOn &&
                oldItem.fileSize == newItem.fileSize &&
                oldItem.name == newItem.name &&
                oldItem.downloadProgress == newItem.downloadProgress
    }

    override fun getChangePayload(oldItem: Download, newItem: Download): Any? {
        if (oldItem.downloadProgress != newItem.downloadProgress) {
            return newItem.downloadProgress
        }

        return super.getChangePayload(oldItem, newItem)
    }
}