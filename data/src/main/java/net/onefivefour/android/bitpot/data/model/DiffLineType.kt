package net.onefivefour.android.bitpot.data.model

/**
 * An enum listing all possible types of a [DiffLine]
 */
enum class DiffLineType {
    LINE_ADDED,
    LINE_REMOVED,
    CONFLICT_DELIMITER,
    LINE_CONFLICT,
    CHUNK_HEADER,
    LINE_NEUTRAL,
    LINE_NUMBERLESS
}
