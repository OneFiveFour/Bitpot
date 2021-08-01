package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.data.model.Ref as AppRef
import net.onefivefour.android.bitpot.data.model.RefType as AppRefType
import net.onefivefour.android.bitpot.network.model.refs.Ref as NetworkRef
import net.onefivefour.android.bitpot.network.model.refs.RefType as NetworkRefType

object RefDummies {

    fun getSimpleAppBranchRef(): AppRef {

        val id = "Ref.id"
        val repoUuid = "Ref.repoUuid"
        val name = "Ref.name"
        val hash = "Ref.hash"
        val type = AppRefType.BRANCH

        return AppRef(
            id,
            repoUuid,
            name,
            hash,
            type
        )
    }

    fun getSimpleAppTagRef(): AppRef {

        val id = "Ref.id"
        val repoUuid = "Ref.repoUuid"
        val name = "Ref.name"
        val hash = "Ref.hash"
        val type = AppRefType.TAG

        return AppRef(
            id,
            repoUuid,
            name,
            hash,
            type
        )
    }

    fun getSimpleNetworkBranchRef() : NetworkRef {

        val type = NetworkRefType.BRANCH
        val name = "Ref.name"
        val target = TargetDummies.getSimpleRefTarget()

        return NetworkRef(type, name, target)
    }


    fun getSerializedSimpleBranchRef() : String {
        return "{\"id\":\"Ref.id\",\"repoUuid\":\"Ref.repoUuid\",\"name\":\"Ref.name\",\"hash\":\"Ref.hash\",\"type\":\"BRANCH\"}"
    }


}
