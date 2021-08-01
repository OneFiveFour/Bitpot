package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.DataNetworkConverter
import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.model.WebHookEvent

/**
 * Converts a String into a app domain [WebHookEvent].
 * Also converts an [WebHookEvent] into a list of Strings to send it as part of a POST or PUT request.
 */
object WebHookEventConverter : DataNetworkConverter<WebHookEvent, List<String>>, NetworkDataConverter<String, WebHookEvent> {

    override fun toNetworkModel(from: WebHookEvent): List<String> {
        return when (from) {
            WebHookEvent.PIPELINE_UPDATES -> listOf(
                "repo:commit_status_updated",
                "repo:commit_status_created"
            )
            WebHookEvent.PULL_REQUEST_UPDATES -> listOf(
                "pullrequest:approved",
                "pullrequest:unapproved",
                "pullrequest:comment_created",
                "pullrequest:created"
            )
            WebHookEvent.ISSUE_UPDATES -> listOf(
                "issue:comment_created",
                "issue:created",
                "issue:updated"
            )
            WebHookEvent.PROJECT_UPDATES -> listOf(
                "project:updated"
            )
            WebHookEvent.REPOSITORY_UPDATES -> listOf(
                "repo:created",
                "repo:deleted",
                "repo:fork",
                "repo:imported",
                "repo:push",
                "repo:transfer",
                "repo:updated"
            )
            WebHookEvent.UNKNOWN -> emptyList()
        }
    }

    @Suppress("ComplexMethod")
    override fun toAppModel(item: String): WebHookEvent {
        return when (item) {

            "issue:comment_created" -> WebHookEvent.ISSUE_UPDATES
            "issue:created" -> WebHookEvent.ISSUE_UPDATES
            "issue:updated" -> WebHookEvent.ISSUE_UPDATES

            "project:updated" -> WebHookEvent.PROJECT_UPDATES

            "pullrequest:approved" -> WebHookEvent.PULL_REQUEST_UPDATES
            "pullrequest:comment_created" -> WebHookEvent.PULL_REQUEST_UPDATES
            "pullrequest:comment_deleted" -> WebHookEvent.PULL_REQUEST_UPDATES
            "pullrequest:comment_updated" -> WebHookEvent.PULL_REQUEST_UPDATES
            "pullrequest:created" -> WebHookEvent.PULL_REQUEST_UPDATES
            "pullrequest:fulfilled" -> WebHookEvent.PULL_REQUEST_UPDATES
            "pullrequest:rejected" -> WebHookEvent.PULL_REQUEST_UPDATES
            "pullrequest:unapproved" -> WebHookEvent.PULL_REQUEST_UPDATES
            "pullrequest:updated" -> WebHookEvent.PULL_REQUEST_UPDATES
            "repo:commit_comment_created" -> WebHookEvent.PULL_REQUEST_UPDATES

            "repo:commit_status_created" -> WebHookEvent.PIPELINE_UPDATES
            "repo:commit_status_updated" -> WebHookEvent.PIPELINE_UPDATES

            "repo:created" -> WebHookEvent.REPOSITORY_UPDATES
            "repo:deleted" -> WebHookEvent.REPOSITORY_UPDATES
            "repo:fork" -> WebHookEvent.REPOSITORY_UPDATES
            "repo:imported" -> WebHookEvent.REPOSITORY_UPDATES
            "repo:push" -> WebHookEvent.REPOSITORY_UPDATES
            "repo:transfer" -> WebHookEvent.REPOSITORY_UPDATES
            "repo:updated" -> WebHookEvent.REPOSITORY_UPDATES

            else -> WebHookEvent.UNKNOWN
        }
    }
}