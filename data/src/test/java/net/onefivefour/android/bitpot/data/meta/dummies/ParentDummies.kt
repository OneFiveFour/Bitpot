package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.comments.Parent as CommentParent

object ParentDummies {

    fun getCommentParent(): CommentParent {
        val id = 1
        return CommentParent(id)
    }
}