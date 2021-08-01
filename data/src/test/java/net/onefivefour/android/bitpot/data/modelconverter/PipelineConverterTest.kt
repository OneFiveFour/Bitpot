package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.meta.dummies.PipelineDummies
import net.onefivefour.android.bitpot.data.model.PipelineState
import net.onefivefour.android.bitpot.data.model.PipelineTarget
import net.onefivefour.android.bitpot.data.model.converter.PipelineConverter
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class PipelineConverterTest {

    private lateinit var sut: PipelineConverter

    private val pipelineDummy = PipelineDummies.getSimplePipeline()

    @Before
    fun setup() {
        sut = PipelineConverter()
    }

    @Test
    fun convert_simplePipeline_returnsConvertedPipeline() {
        val result = sut.toAppModel(pipelineDummy)

        assertEquals(1, result.buildNumber)
        assertEquals(1572278556L, result.createdOn.epochSecond)
        assertEquals("Repository.uuid", result.repoUuid)
        assertEquals("Pipeline.uuid", result.uuid)

        assertThat(result.target, instanceOf(PipelineTarget.Commit::class.java))
        assertThat(result.state, instanceOf(PipelineState.IN_PROGRESS::class.java))
    }
}
