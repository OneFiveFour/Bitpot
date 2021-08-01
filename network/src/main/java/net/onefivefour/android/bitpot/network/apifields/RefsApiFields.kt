package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.refs.Ref] network model class.
 */
class RefsApiFields : PagedFields() {
    override fun getFields(): Set<String> {
        return getPagingFields().union(
            setOf(
                "values.name",
                "values.target.hash",
                "values.target.repository.uuid",
                "values.type"
            )
        )
    }
}
