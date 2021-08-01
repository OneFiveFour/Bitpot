package net.onefivefour.android.bitpot.databinding

import androidx.databinding.BindingAdapter
import androidx.paging.LoadState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import net.onefivefour.android.bitpot.data.common.DataState


/**
 * Set the loading animation of a [SwipeRefreshLayout] using a [DataState].
 */
@BindingAdapter("refreshing")
fun isRefreshing(swipeRefreshLayout: SwipeRefreshLayout, refreshState: DataState?) {
    swipeRefreshLayout.isRefreshing = refreshState == DataState.LOADING
}

/**
 * Set the loading animation of a [SwipeRefreshLayout] using a [DataState].
 */
@BindingAdapter("refreshing")
fun isRefreshing(swipeRefreshLayout: SwipeRefreshLayout, loadState: LoadState?) {
    swipeRefreshLayout.isRefreshing = loadState == LoadState.Loading
}
