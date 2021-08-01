package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.repositories.Repository] network model class.
 */
class RepositoriesApiFields : PagedFields() {
    override fun getFields(): Set<String> {
        return getPagingFields().union(
            listOf(
                "values.full_name",
                "values.is_private",
                "values.links.avatar.href",
                "values.name",
                "values.updated_on",
                "values.uuid",
                "values.workspace.uuid",
                "values.mainbranch.name"
            )
        )
    }
}