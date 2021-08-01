package net.onefivefour.android.bitpot.network.apifields

/**
 * A list of all api fields we want
 * for the [net.onefivefour.android.bitpot.network.model.comments.Comment] network model class.
 */
class CommentApiFields : ApiFieldsConfig() {
    override fun getFields(): Set<String> {
        return setOf(
            "content.raw",
            "created_on",
            "id",
            "inline",
            "parent.id",
            "pullrequest.id",
            "updated_on",
            "user.display_name",
            "user.links.avatar.href",
            "user.account_id"
        )
    }
}