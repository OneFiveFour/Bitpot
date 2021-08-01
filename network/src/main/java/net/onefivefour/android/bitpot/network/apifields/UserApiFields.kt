package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.workspace.Workspace] network model class.
 */
class UserApiFields : ApiFieldsConfig() {
    override fun getFields(): Set<String> {
        return setOf("account_id")
    }
}
