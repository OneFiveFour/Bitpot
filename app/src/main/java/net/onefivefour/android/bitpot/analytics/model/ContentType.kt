package net.onefivefour.android.bitpot.analytics.model

/**
 * An enum of all content types that can be selected as part of a
 * [FirebaseAnalytics.Event#SELECT_CONTENT] event.
 */
enum class ContentType(val analyticsTag: String) {
    WORKSPACE("workspace"),
    PIPELINE("pipeline"),
    DOWNLOAD("download"),
    SEND_FEEDBACK("send_feedback")
}
