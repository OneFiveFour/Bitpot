package net.onefivefour.android.bitpot.data.database.converter

import net.onefivefour.android.bitpot.data.model.PipelineState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PipelineStateConverterTest {

    private lateinit var sut: PipelineStateConverter

    @Before
    fun setUp() {
        sut = PipelineStateConverter()
    }

    @Test
    fun stringToPipelineState_correctPipelineState_returnsCorrectString() {
        PipelineState.values().forEach {
            val result = sut.pipelineStateToString(it)
            assertEquals(it.name, result)
        }
    }

    @Test
    fun pipelineStateToString_correctString_returnsCorrectPipelineState() {
        PipelineState.values().forEach {
            val result = sut.stringToPipelineState(it.name)
            assertEquals(it, result)
        }
    }
}