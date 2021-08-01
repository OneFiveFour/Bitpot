package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.meta.dummies.PostMergeDummies
import net.onefivefour.android.bitpot.data.model.converter.MergeConverter
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MergeConverterTest {

    private lateinit var sut: MergeConverter

    @Before
    fun setup() {
        sut = MergeConverter()
    }

    @Test
    fun toAppModel_correctAppMerge_returnsCorrectNetworkModel() {
        val postMerge = PostMergeDummies.getPostMerge()

        val result = sut.toNetworkModel(postMerge)

        assertEquals(postMerge.commitMessage, result.commitMessage)
        assertEquals(postMerge.closeSourceBranch, result.closeSourceBranch)
        assertEquals("merge_commit", result.mergeStrategy)
    }
}