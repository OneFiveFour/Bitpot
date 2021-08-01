package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.extensions.toMD5
import net.onefivefour.android.bitpot.data.model.RefType
import net.onefivefour.android.bitpot.data.model.Ref as AppRef
import net.onefivefour.android.bitpot.network.model.refs.Ref as NetworkRef
import net.onefivefour.android.bitpot.network.model.refs.RefType as NetworkRefType

/**
 * Converts a [NetworkRef] into a app domain [net.onefivefour.android.bitpot.data.model.Ref].
 */
class RefConverter : NetworkDataConverter<NetworkRef, AppRef> {

    override fun toAppModel(item: NetworkRef): AppRef {

        val type = when (item.type) {
            NetworkRefType.BRANCH -> RefType.BRANCH
            NetworkRefType.TAG -> RefType.TAG
            NetworkRefType.ALL -> RefType.ALL
        }
        val id = (item.target.hash + item.name + item.type).toMD5()
        val hash = item.target.hash
        val repoUuid = item.target.repository.uuid

        return AppRef(
            id,
            repoUuid,
            item.name,
            hash,
            type
        )
    }
}
