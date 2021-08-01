package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.workspace.Workspace] network model class.
 */
class WebHookApiFields : ApiFieldsConfig() {
    override fun getFields(): Set<String> {
        return setOf(
            "created_at",
            "events",
            "subject.uuid",
            "url",
            "uuid"
        )
    }
}
