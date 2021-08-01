package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.data.model.CommentAuthor

object CommentAuthorDummies {
    fun getSimpleCommentAuthor(): CommentAuthor {

        val uuid = "CommentAuthor.uuid"
        val avatarUrl = StringDummies.getDownloadUrl()
        val name = StringDummies.getDisplayName()

        return CommentAuthor(uuid, avatarUrl, name)
    }

}