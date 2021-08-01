package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.downloads.Download] network model class.
 */
class DownloadsApiFields : PagedFields() {
    override fun getFields(): Set<String> {
        return getPagingFields().union(
            setOf(
                "values.created_on",
                "values.links.self.href",
                "values.name",
                "values.size"
            )
        )
    }
}
