package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.workspace.Workspace] network model class.
 */
class PullRequestApiFields : ApiFieldsConfig() {
    override fun getFields(): Set<String> {
        return setOf(
            "id",
            "participants.approved",
            "participants.user.links.avatar.href",
            "participants.user.account_id",
            "rendered.description.raw",
            "state",
            "title"
        )
    }
}
