package net.onefivefour.android.bitpot.data.model

import android.text.SpannableString

/**
 * Describes a diff of a File between two branches.
 */
data class FileDiff(
    val filePath: SpannableString,
    val longestLine: Int,

    // mutable to set a new list for correct DiffUtil (see DiffViewModel)
    var code: List<DiffLine>,

    // mutable to set the list of comments as soon as we get them from the backend.
    var comments: List<Comment>,
    val type: DiffType,
    val hasMergeConflict: Boolean,
    val lineNumberStringLength: Int
): DiffItem