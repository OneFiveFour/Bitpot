package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.pipelines.Pipeline] network model class.
 */
class WorkspacesApiFields : PagedFields() {
    override fun getFields(): Set<String> {
        return getPagingFields().union(
            listOf(
                "values.links.avatar.href",
                "values.name",
                "values.uuid"
            )
        )
    }
}