package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.data.model.RefType

object RefTypeDummies {

    fun getBranchRefType(): RefType {
        return RefType.BRANCH
    }

    fun getTagRefType(): RefType {
        return RefType.TAG
    }

    fun getSerializedTagRefType(): String {
        return "TAG"
    }

}
