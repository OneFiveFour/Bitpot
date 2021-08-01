package net.onefivefour.android.bitpot.common

/**
 * If your fragment needs to show SnackBars,
 * let its activity implement this interface.
 */
interface SnackBarPresenter {
    fun showSnackBar(message: String)
}