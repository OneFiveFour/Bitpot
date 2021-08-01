package net.onefivefour.android.bitpot.analytics

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * This event tracker should be used in the app to send new events to firebase.
 * It can be injected using Koin.
 */
class EventTracker(private val firebase: FirebaseAnalytics) {

    /**
     * send the given event to Firebase Analytics.
     */
    fun sendEvent(event: AnalyticsEvent) {
        when (event) {

            is AnalyticsEvent.Login -> firebase.logEvent(FirebaseAnalytics.Event.LOGIN, bundleOf(
                FirebaseAnalytics.Param.METHOD to "o_auth",
                AnalyticsCustomParam.ACTION_RESULT to event.outcome.analyticsTag
            ))

            is AnalyticsEvent.SelectContent -> firebase.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleOf(
                FirebaseAnalytics.Param.CONTENT_TYPE to event.contentType.analyticsTag
            ))

            is AnalyticsEvent.TogglePipelineNotifications -> firebase.logEvent(AnalyticsCustomEvent.PUSH_NOTIFICATIONS, bundleOf(
                AnalyticsCustomParam.ACTION_NAME to "toggle_pipeline_notifications",
                AnalyticsCustomParam.ACTION_VALUE_1 to event.toggleAction.analyticsTag
            ))

            is AnalyticsEvent.TogglePullRequestNotifications -> firebase.logEvent(AnalyticsCustomEvent.PUSH_NOTIFICATIONS, bundleOf(
                AnalyticsCustomParam.ACTION_NAME to "toggle_pull_request_notifications",
                AnalyticsCustomParam.ACTION_VALUE_1 to event.toggleAction.analyticsTag
            ))

            is AnalyticsEvent.ForcedDeactivationOfNotifications -> firebase.logEvent(AnalyticsCustomEvent.PUSH_NOTIFICATIONS, bundleOf(
                AnalyticsCustomParam.ACTION_NAME to "forced_deactivation"
            ))

            is AnalyticsEvent.NoPermissionDialog -> firebase.logEvent(AnalyticsCustomEvent.PUSH_NOTIFICATIONS, bundleOf(
                AnalyticsCustomParam.ACTION_NAME to "no_permission_dialog"
            ))

            is AnalyticsEvent.FileShared -> firebase.logEvent(FirebaseAnalytics.Event.SHARE, bundleOf(
                FirebaseAnalytics.Param.CONTENT_TYPE to "file"
            ))
        }
    }
}