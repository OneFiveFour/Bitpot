package net.onefivefour.android.bitpot.analytics.model

/**
 * An enum listing all possible outcomes of the user login
 */
enum class LoginOutcome(val analyticsTag: String) {
    SUCCESS("success"),
    ERROR("error")
}