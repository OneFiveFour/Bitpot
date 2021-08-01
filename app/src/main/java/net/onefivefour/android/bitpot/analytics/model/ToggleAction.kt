package net.onefivefour.android.bitpot.analytics.model

/**
 * If we are sending any analytics event that involves turning something on or off,
 * use this action as event parameter.
 */
enum class ToggleAction(val analyticsTag: String) {
    ON("on"),
    OFF("off")
}
