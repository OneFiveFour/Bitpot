package net.onefivefour.android.bitpot.data.common

/**
 * Implement this interface to convert a data model into a network model
 */
interface DataNetworkConverter<FROM, TO> {
    fun toNetworkModel(from: FROM): TO
}