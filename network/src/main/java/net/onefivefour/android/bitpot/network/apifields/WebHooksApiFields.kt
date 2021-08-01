package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.webhooks.WebHooks] network model class.
 */
class WebHooksApiFields : PagedFields() {
    override fun getFields(): Set<String> {
        return getPagingFields().union(
            setOf(
                "values.created_at",
                "values.description",
                "values.events",
                "values.subject.uuid",
                "values.url",
                "values.uuid"
            )
        )
    }
}