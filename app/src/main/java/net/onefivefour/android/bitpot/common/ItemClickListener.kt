package net.onefivefour.android.bitpot.common

import android.view.View

/**
 * A ClickListener defining all click events that are possible on a list item.
 * Make sure that you always check for the right class for the clickedItem parameter.
 */
interface ItemClickListener {
    fun onClick(clickedView: View, clickedItem: Any)
}