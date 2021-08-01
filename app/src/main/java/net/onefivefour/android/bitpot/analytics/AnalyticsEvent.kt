package net.onefivefour.android.bitpot.analytics

import net.onefivefour.android.bitpot.analytics.model.ContentType
import net.onefivefour.android.bitpot.analytics.model.LoginOutcome
import net.onefivefour.android.bitpot.analytics.model.ToggleAction

/**
 * These are all the analytics events that the app can send.
 * Use this class together with the [EventTracker] to send events to Firebase.
 */
sealed class AnalyticsEvent {

    /**
     * An Analytics Event for Logins
     */
    class Login(val outcome: LoginOutcome) : AnalyticsEvent()

    /**
     * An Analytics Event for when the user selects some kind of content in the app
     */
    class SelectContent(val contentType: ContentType) : AnalyticsEvent()

    /**
     * An Analytics Event for when the user toggles pipeline notifications on or off.
     */
    class TogglePipelineNotifications(val toggleAction: ToggleAction) : AnalyticsEvent()

    /**
     * An Analytics Event for when the user toggles pull request notifications on or off.
     */
    class TogglePullRequestNotifications(val toggleAction: ToggleAction) : AnalyticsEvent()

    /**
     * An Analytics Event for when push notifications are turned off by the app.
     * This may happen because no web hooks were found on the server, the user is not admin or
     * due to an unknown error.
     */
    object ForcedDeactivationOfNotifications : AnalyticsEvent()

    /**
     * An Analytics Event for when the user shares a file.
     */
    object FileShared : AnalyticsEvent()

    /**
     * An Analytics Event for when the user is not admin of a repository, but wants to create a web hook.
     */
    object NoPermissionDialog : AnalyticsEvent()
}