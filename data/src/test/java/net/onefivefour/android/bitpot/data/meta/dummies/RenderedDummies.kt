package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pullrequest.Rendered

object RenderedDummies {
    fun getRendered(): Rendered {
        val description = DescriptionDummies.getDescription()
        return Rendered(description)
    }

}
