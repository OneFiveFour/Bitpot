package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.comments.Inline

object InlineDummies {

    fun getInline(): Inline {
        val from = 1
        val path = StringDummies.getPath()
        val to = null

        return Inline(
            from,
            path,
            to
        )
    }
}