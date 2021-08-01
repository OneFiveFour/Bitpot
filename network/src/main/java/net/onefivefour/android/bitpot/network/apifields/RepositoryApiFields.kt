package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.repositories.Repository] network model class.
 */
class RepositoryApiFields : ApiFieldsConfig() {
    override fun getFields(): Set<String> {
        return setOf(
            "full_name",
            "is_private",
            "links.avatar.href",
            "name",
            "updated_on",
            "uuid",
            "workspace.uuid",
            "mainbranch.name"
        )
    }
}