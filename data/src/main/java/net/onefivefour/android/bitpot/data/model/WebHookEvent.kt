package net.onefivefour.android.bitpot.data.model

/**
 * An enum of all possible WebHookEvents as presented in the UI.
 * These events are getting mapped to the internal Bitbucket values in [net.onefivefour.android.bitpot.data.model.converter.WebHookEventConverter]
 */
enum class WebHookEvent {
    PIPELINE_UPDATES,
    PULL_REQUEST_UPDATES,
    UNKNOWN,
    ISSUE_UPDATES,
    PROJECT_UPDATES,

    REPOSITORY_UPDATES
}
