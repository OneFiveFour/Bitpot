package net.onefivefour.android.bitpot.data.model

/**
 * All types that a [Source] can represent.
 * This may be e.g. a file or a directory.
 */
enum class SourceType {

    /**
     * Inserted to every directory (except root),
     * to navigate up from it.
     */
    FOLDER_UP,

    /**
     * A directory.
     */
    DIRECTORY,

    /**
     * A file.
     */
    FILE,

    /**
     * Unknown SourceType that came from the API.
     */
    UNKNOWN
}
