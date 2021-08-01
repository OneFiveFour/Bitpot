package net.onefivefour.android.bitpot.data.common

/**
 * Implement this interface in conjunction with [net.onefivefour.android.bitpot.data.common.NetworkDataConversion]
 * to provide a converter between network model and app model
 *
 * @param FROM the model class to convert from (network response)
 * @param TO the model class to convert to (app domain)
 */
interface NetworkDataConverter<FROM,TO> {
    fun toAppModel(item: FROM) : TO
}