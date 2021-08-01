package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.sources.Source] network model class.
 */
class SourcesApiFields : PagedFields() {
    override fun getFields(): Set<String> {
        return getPagingFields().union(
            listOf(
                "values.commit.hash",
                "values.path",
                "values.size",
                "values.type"
            )
        )
    }
}