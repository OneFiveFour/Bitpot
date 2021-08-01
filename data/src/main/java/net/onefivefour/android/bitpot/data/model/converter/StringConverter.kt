package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter

/**
 * A very simple pseudo-converter.
 * It just returns the String that it is receiving. This converter
 * is necessary when fetching the raw String response from the backend.
 * (e.g. to fetch the content of a file).
 */
class StringConverter : NetworkDataConverter<String, String> {
    override fun toAppModel(item: String): String {
        return item
    }

}