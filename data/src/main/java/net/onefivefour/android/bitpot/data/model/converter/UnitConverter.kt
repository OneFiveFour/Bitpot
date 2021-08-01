package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter

/**
 * Simple Converter for network calls that come without payload.
 * Usually together with a 204 HTTP code.
 */
class UnitConverter: NetworkDataConverter<Unit, Unit> {
    override fun toAppModel(item: Unit) = item
}