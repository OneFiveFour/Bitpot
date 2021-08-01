package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.pipelines.Pipeline] network model class.
 */
class PipelinesApiFields : PagedFields() {
    override fun getFields(): Set<String> {
        return getPagingFields().union(
            listOf(
                "values.build_number",
                "values.created_on",
                "values.repository.uuid",
                "values.state",
                "values.target.commit.message",
                "values.target.destination",
                "values.target.ref_name",
                "values.target.source",
                "values.target.type",
                "values.uuid"
            )
        )
    }
}