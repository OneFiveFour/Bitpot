package net.onefivefour.android.bitpot.databinding

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.Download

/**
 * Set an [ItemClickListener] for [Download] to a view.
 */
@BindingAdapter("downloadClick", "download")
fun setDownloadClick(view: View, clickListener: ItemClickListener, download: Download) {
    view.setOnClickListener {
        clickListener.onClick(view, download)
    }
}

@BindingAdapter("downloadProgress")
fun setDownloadProgress(progressBar: ProgressBar, progress: Float) {
    progressBar.isIndeterminate = progress == -1f
    progressBar.progress = progress.toInt()
}