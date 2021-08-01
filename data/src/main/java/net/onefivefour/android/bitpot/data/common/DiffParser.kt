package net.onefivefour.android.bitpot.data.common

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import net.onefivefour.android.bitpot.data.model.DiffLine
import net.onefivefour.android.bitpot.data.model.DiffLineType
import net.onefivefour.android.bitpot.data.model.DiffType
import net.onefivefour.android.bitpot.data.model.FileDiff
import org.koin.core.KoinComponent


/**
 * This class takes a String as input which should represent a git-style diff.
 * For more information about such a diff see: https://www.atlassian.com/git/tutorials/saving-changes/git-diff
 *
 * By calling [parse] this class creates a [FileDiff] out of the given String
 * which can then be used for further purposes.
 */
class DiffParser(diff: String) : KoinComponent {

    /**
     * The StringBuilder to create the content Strings of each DiffLine
     */
    private val sb = StringBuilder()

    /**
     * The list of lines of this FileDiff
     */
    private val content = arrayListOf<DiffLine>()

    /**
     * A list of strings. Each String is one line of the given diff string.
     */
    private val lines: List<String> = diff.lines()

    /**
     * The index of the first line that is the header of a diff chunk.
     */
    private val firstChunkIndex = lines.indexOfFirst { it.startsWith("@@") }

    /**
     * the [DiffType] of the current diff.
     * Used to create the final Diff object.
     */
    private lateinit var diffType: DiffType

    /**
     * A flag to show if this diff has currently a merge conflict.
     * Used to create the final Diff object.
     */
    private var hasMergeConflict = false

    /**
     * A Flag to tell the parse method how to highlight
     * the current code line.
     */
    private var conflictModeEnabled = false

    /**
     * The longest line within the diff.
     */
    private var longestLine = 0

    /**
     * The highest line number within all the chunks of this diff.
     */
    private val highestLineNumberCharCount = getLineNumberCharCount()

    /**
     * The number of chars that the line numbers occupy.
     * Counts two times the chars needed for the numbers themselves
     * plus 1 for the dividing space
     * plus 1 for the dividing pipe
     */
    private val lineNumberStringLength = 2 * highestLineNumberCharCount + 1 + 1

    /**
     * The current line number on the left column.
     */
    private var leftLineNumber = 0

    /**
     * The current line number on the right column.
     */
    private var rightLineNumber = 0

    @Suppress("ComplexMethod")
    fun parse(): FileDiff {

        evaluateInput()

        // extract the file path of the diff
        val filePath = createFilePath()

        if (firstChunkIndex < 0) {
            // It is possible to have no diff chunk header at all, when an empty file was added or removed.
            return FileDiff(filePath, longestLine, emptyList(), emptyList(), diffType, hasMergeConflict, 0)
        }

        // iterate over every line of the diff
        for (i in firstChunkIndex until lines.size - 1) {
            val line = lines[i]

            // add spaces to the line to make every line the same length.
            val paddedLine = when {
                line.startsWith("@@") -> line.padEnd(longestLine + lineNumberStringLength)
                else -> line.padEnd(longestLine)
            }

            // depending on the current type of line, add the line numbers to the string builder
            // and create SpannableStrings for later visual computation.
            // While parsing a section of a merge conflict, we are in "conflictModeEnabled".
            // That means that all lines are highlighted in a light yellow color as long as
            // the merge conflict end-line did not occur.
            when {
                line.startsWith("@@") -> addChunkHeader(paddedLine)
                line.startsWith("+=======") -> addLineConflictMiddle(paddedLine)
                line.startsWith("+>>>>>>>") -> addLineConflictEnd(paddedLine)
                line.startsWith("+<<<<<<<") -> addLineConflictStart(paddedLine)
                line.startsWith("+") -> addLineAdded(paddedLine)
                line.startsWith("-") -> addLineRemoved(paddedLine)
                line.startsWith("\\ No newline") -> addNumberlessLine(paddedLine)
                else -> addNeutralLine(paddedLine)
            }
        }

        // Diff was parsed and handled successfully, create and return Diff
        return FileDiff(filePath, longestLine, content, emptyList(), diffType, hasMergeConflict, lineNumberStringLength)
    }

    private fun createFilePath(): SpannableString {
        val path = lines.first()
            .split(" ")
            .last()
            .removePrefix("b/")
        val startIndex = path.lastIndexOf("/") + 1
        val endIndex = path.length
        val spannablePath = SpannableString(path)
        spannablePath.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannablePath
    }

    private fun evaluateInput() {
        if (lines.isNullOrEmpty() || lines.size < 2) {
            throw IllegalArgumentException("Could not parse diff: Incorrect or no content lines were given.")
        }

        diffType = when {
            lines[1].startsWith("new") -> DiffType.FILE_ADDED
            lines[1].startsWith("deleted") -> DiffType.FILE_REMOVED
            else -> DiffType.FILE_MODIFIED
        }

        longestLine = when {
            firstChunkIndex > 0 -> lines.subList(firstChunkIndex, lines.size - 1).maxByOrNull { it.length }?.length ?: 0
            else -> 0
        }
    }

    /**
     * add SpanInfo and line numbers for a new chunk header.
     */
    private fun addChunkHeader(line: String) {
        val headerElements = line.split(" ")
        val leftLineInfo = headerElements[1].substring(1).split(",")
        val rightLineInfo = headerElements[2].substring(1).split(",")
        leftLineNumber = leftLineInfo.first().toInt()
        rightLineNumber = rightLineInfo.first().toInt()

        content.add(DiffLine(DiffLineType.CHUNK_HEADER, -1, -1, line))
    }

    /**
     * Add SpanInfo and line numbers for line that signals the beginning of a merge conflict.
     */
    private fun addLineConflictStart(line: String) {
        hasMergeConflict = true
        conflictModeEnabled = true

        sb.clear()
        addLineNumbersRight()
        sb.append(line)

        content.add(DiffLine(DiffLineType.CONFLICT_DELIMITER, -1, rightLineNumber, sb.toString()))

        rightLineNumber++
    }

    /**
     * Add SpanInfo and line numbers for line that signals the middle of a merge conflict.
     * I.e. the change from destination code to source code.
     */
    private fun addLineConflictMiddle(line: String) {
        sb.clear()
        addLineNumbersRight()
        sb.append(line)

        content.add(DiffLine(DiffLineType.CONFLICT_DELIMITER, -1, rightLineNumber, sb.toString()))

        rightLineNumber++
    }

    /**
     * Add SpanInfo and line numbers for line that signals the end of a merge conflict.
     */
    private fun addLineConflictEnd(line: String) {
        conflictModeEnabled = false

        sb.clear()
        addLineNumbersRight()
        sb.append(line)

        content.add(DiffLine(DiffLineType.CONFLICT_DELIMITER, -1, rightLineNumber, sb.toString()))

        rightLineNumber++
    }

    /**
     * Add SpanInfo and line numbers for a new added line.
     */
    private fun addLineAdded(line: String) {
        sb.clear()
        addLineNumbersRight()
        sb.append(line)

        content.add(DiffLine(DiffLineType.LINE_ADDED, -1, rightLineNumber, sb.toString()))

        rightLineNumber++
    }

    /**
     * Add SpanInfo and line numbers for a removed line.
     */
    private fun addLineRemoved(line: String) {
        sb.clear()
        addLineNumbersLeft()
        sb.append(line)

        content.add(DiffLine(DiffLineType.LINE_REMOVED, leftLineNumber, -1, sb.toString()))

        leftLineNumber++
    }

    /**
     * Add SpanInfo and line numbers for a removed line.
     */
    private fun addNeutralLine(line: String) {
        sb.clear()
        addLineNumbers()
        sb.append(line)

        val lineType = when {
            conflictModeEnabled -> DiffLineType.LINE_CONFLICT
            else -> DiffLineType.LINE_NEUTRAL
        }

        content.add(DiffLine(lineType, leftLineNumber, rightLineNumber, sb.toString()))

        leftLineNumber++
        rightLineNumber++
    }

    /**
     * Add SpanInfo a line without numbers (e.g. meta info like "no new line").
     */
    private fun addNumberlessLine(line: String) {
        sb.clear()
        sb.append(line)
        content.add(DiffLine(DiffLineType.LINE_NUMBERLESS, -1, -1, sb.toString()))
    }

    /**
     * Append the line numbers only on the right side of the 2 columns.
     * Usually used for added lines.
     */
    private fun addLineNumbersRight() {
        sb.append("".padStart(highestLineNumberCharCount))
        sb.append(" ")
        sb.append(rightLineNumber.toString().padStart(highestLineNumberCharCount))
        sb.append(" ")
    }

    /**
     * Append the line numbers only on the left side of the 2 columns.
     * Usually used for removed lines.
     */
    private fun addLineNumbersLeft() {
        sb.append(leftLineNumber.toString().padStart(highestLineNumberCharCount))
        sb.append(" ")
        sb.append("".padEnd(highestLineNumberCharCount))
        sb.append(" ")
    }

    /**
     * Append the line numbers on both sides of the 2 columns.
     */
    private fun addLineNumbers() {
        sb.append(leftLineNumber.toString().padStart(highestLineNumberCharCount))
        sb.append(" ")
        sb.append(rightLineNumber.toString().padStart(highestLineNumberCharCount))
        sb.append(" ")
    }

    /**
     * Uses the line number information of each diff chunk to calculate
     * the highest line number of all chunks.
     */
    private fun getLineNumberCharCount(): Int {
        var result = 0
        lines
            .filter { it.startsWith("@@") }
            .forEach { line ->
                val headerElements = line.split(" ")
                val leftLineInfo = headerElements[1].substring(1).split(",")
                val rightLineInfo = headerElements[2].substring(1).split(",")

                val highestLineNumberLeft = leftLineInfo.sumOf { it.toInt() }
                val highestLineNumberRight = rightLineInfo.sumOf { it.toInt() } - 1

                result = maxOf(result, highestLineNumberLeft, highestLineNumberRight)
            }
        return result.toString().length
    }
}