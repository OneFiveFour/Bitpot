package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.comments.Content

object ContentDummies {

    fun getContent(): Content {
        val raw = StringDummies.getRawString()
        return Content(raw)
    }

}
