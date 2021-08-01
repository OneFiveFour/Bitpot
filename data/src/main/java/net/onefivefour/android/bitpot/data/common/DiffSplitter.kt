package net.onefivefour.android.bitpot.data.common


/**
 * This object is used to separate a text file containing git diffs of several files into
 * a list of Strings. One for each git diff.
 */
object DiffSplitter {

    private const val DIFF_DELIMITER = "diff --git"

    /**
     * @return a list of Strings. Each String represents one git diff found in the given [diffs]
     */
    fun split(diffs: String) : List<String> {
        return diffs
            .split(DIFF_DELIMITER)
            .filter { it.isNotEmpty() }
            .map { DIFF_DELIMITER + it }
    }
}