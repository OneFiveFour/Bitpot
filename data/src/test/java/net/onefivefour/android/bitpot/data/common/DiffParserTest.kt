package net.onefivefour.android.bitpot.data.common

import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import net.onefivefour.android.bitpot.data.meta.FileLoader
import net.onefivefour.android.bitpot.data.model.DiffType
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class DiffParserTest: KoinTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            startKoin { modules(dataModuleTesting) }
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            stopKoin()
        }
    }

    @Test
    fun parse_fullDiffList_correctResults() {
        val diff = FileLoader.getJson("git_diff.txt")
        val splitDiff = DiffSplitter.split(diff)

        val result = splitDiff.map { DiffParser(it).parse() }

        Assert.assertEquals(11, result.size)
        Assert.assertEquals(DiffType.FILE_MODIFIED, result[0].type)
        Assert.assertEquals(true, result[0].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_MODIFIED, result[1].type)
        Assert.assertEquals(true, result[1].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_ADDED, result[2].type)
        Assert.assertEquals(false, result[2].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_ADDED, result[3].type)
        Assert.assertEquals(false, result[3].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_ADDED, result[4].type)
        Assert.assertEquals(false, result[4].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_ADDED, result[5].type)
        Assert.assertEquals(false, result[5].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_REMOVED, result[6].type)
        Assert.assertEquals(false, result[6].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_REMOVED, result[7].type)
        Assert.assertEquals(false, result[7].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_ADDED, result[8].type)
        Assert.assertEquals(false, result[8].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_MODIFIED, result[9].type)
        Assert.assertEquals(false, result[9].hasMergeConflict)

        Assert.assertEquals(DiffType.FILE_REMOVED, result[10].type)
        Assert.assertEquals(false, result[10].hasMergeConflict)

    }

    @Test
    fun parse_fullDiffList2_correctResults() {
        val diff = FileLoader.getJson("git_diff_2.txt")
        val splitDiff = DiffSplitter.split(diff)

        val result = splitDiff.map { DiffParser(it).parse() }

        Assert.assertEquals(34, result.size)
    }

}