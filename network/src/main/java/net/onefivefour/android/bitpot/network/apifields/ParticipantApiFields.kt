package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.workspace.Workspace] network model class.
 */
class ParticipantApiFields : ApiFieldsConfig() {
    override fun getFields(): Set<String> {
        return setOf(
            "approved",
            "user.links.avatar.href",
            "user.account_id"
        )
    }
}
