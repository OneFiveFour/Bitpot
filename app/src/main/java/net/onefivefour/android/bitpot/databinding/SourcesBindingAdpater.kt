package net.onefivefour.android.bitpot.databinding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.Source
import net.onefivefour.android.bitpot.data.model.SourceType

/**
 * Set an [ItemClickListener] for [Source] to a view.
 */
@BindingAdapter("sourceClick", "source")
fun setSourceClick(view: View, clickListener: ItemClickListener, source: Source) {
    view.setOnClickListener {
        clickListener.onClick(view, source)
    }
}


@BindingAdapter("sourceIcon")
fun setSourceIcon(imageView: ImageView, sourceIcon: SourceType) {
    val drawable = when (sourceIcon) {
        SourceType.DIRECTORY -> R.drawable.ic_folder
        SourceType.FILE -> R.drawable.ic_file
        SourceType.FOLDER_UP -> R.drawable.ic_folder_up
        SourceType.UNKNOWN -> R.drawable.ic_file
    }

    imageView.setImageResource(drawable)
}
