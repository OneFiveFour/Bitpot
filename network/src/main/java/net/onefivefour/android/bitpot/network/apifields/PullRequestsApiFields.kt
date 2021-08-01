package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.pullrequests.PullRequest] network model class.
 */
class PullRequestsApiFields : PagedFields() {
    override fun getFields(): Set<String> {
        return getPagingFields().union(
            setOf(
                "values.author.links.avatar.href",
                "values.destination.branch.name",
                "values.destination.repository.uuid",
                "values.id",
                "values.links.self.href",
                "values.source.branch.name",
                "values.state",
                "values.updated_on"
            )
        )
    }
}
