package net.onefivefour.android.bitpot.data.model

/**
 * All states of connections that can be emitted as ConnectionLiveData.
 * This one is used within the data module
 */
enum class ConnectionState {
    IS_CONNECTED,
    IS_DISCONNECTED
}