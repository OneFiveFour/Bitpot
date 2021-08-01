package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pullrequest.Description

object DescriptionDummies {

    fun getDescription(): Description {
        val raw = StringDummies.getRawString()
        return Description(raw)
    }

}
