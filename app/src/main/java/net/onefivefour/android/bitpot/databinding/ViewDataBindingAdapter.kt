package net.onefivefour.android.bitpot.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.common.DataState
import net.onefivefour.android.bitpot.extensions.fadeIn
import net.onefivefour.android.bitpot.extensions.fadeOut


/**
 * Sets the loading animation to indicate network activity
 */
@BindingAdapter("loading")
fun isLoading(view: View, loadingState: DataState?) {
    when (loadingState) {
        DataState.LOADING -> view.fadeIn()
        else -> view.fadeOut()
    }
}

/**
 * Sets the loading animation to indicate network activity
 */
@BindingAdapter("loading")
fun isLoading(view: View, isLoading: Boolean?) {
    when (isLoading) {
        true -> view.fadeIn()
        else -> view.fadeOut()
    }
}

/**
 * A common binding adapter to use a boolean for hiding and showing a View.
 * This one is setting the View to GONE. Use "binding:isInvisible" to set it to INVISIBLE.
 */
@BindingAdapter("isVisible")
fun setVisibilityGone(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}


@BindingAdapter("clickListener", "clickListenerItem")
fun setClickListener(view: View, clickListener: ItemClickListener?, item: Any?) {
    if (clickListener == null || item == null) return
    view.setOnClickListener {
        clickListener.onClick(view, item)
    }
}