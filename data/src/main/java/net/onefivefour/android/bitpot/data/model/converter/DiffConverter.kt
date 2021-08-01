package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.DiffParser
import net.onefivefour.android.bitpot.data.common.DiffSplitter
import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.model.FileDiff

/**
 * Converts the complete String of the git diff result into a list of [FileDiff]s
 */
class DiffConverter : NetworkDataConverter<String, List<FileDiff>> {

    override fun toAppModel(item: String): List<FileDiff> {
        val diffs = DiffSplitter.split(item)
        return diffs.map { DiffParser(it).parse() }
    }
}
