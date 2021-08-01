package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.comments.Comment] network model class.
 */
class CommentsApiFields : PagedFields() {
    override fun getFields(): Set<String> {
        return getPagingFields().union(
            listOf(
                "values.content.raw",
                "values.created_on",
                "values.id",
                "values.inline",
                "values.parent.id",
                "values.pullrequest.id",
                "values.updated_on",
                "values.user.display_name",
                "values.user.links.avatar.href",
                "values.user.account_id"
            )
        )
    }
}