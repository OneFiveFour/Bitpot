package net.onefivefour.android.bitpot.data.common

/**
 * An enum of all possible HTTP status codes.
 */
enum class HttpStatus(val code: Int) {

    CONTINUE(100),
    PROCESSING(102),

    FORBIDDEN(403),

    UNKNOWN(0)
}