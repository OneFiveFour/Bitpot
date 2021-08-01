package net.onefivefour.android.bitpot.data.common

import net.onefivefour.android.bitpot.data.meta.FileLoader
import org.junit.Assert
import org.junit.Test

class DiffSplitterTest {


    @Test
    fun split_correctDiff_correctResult() {
        val diff = FileLoader.getJson("git_diff.txt")

        val result = DiffSplitter.split(diff)

        Assert.assertEquals(11, result.size)
    }


}