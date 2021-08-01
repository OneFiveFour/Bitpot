package net.onefivefour.android.bitpot.data.database.converter

import net.onefivefour.android.bitpot.data.model.PullRequestState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PullRequestStateConverterTest {

    private lateinit var sut: PullRequestStateConverter

    @Before
    fun setUp() {
        sut = PullRequestStateConverter()
    }

    @Test
    fun stringToPullRequestState() {
        PullRequestState.values().forEach {
            val result = sut.stringToPullRequestState(it.name)
            assertEquals(it, result)
        }
    }

    @Test
    fun pullRequestStateToString() {
        PullRequestState.values().forEach {
            val result = sut.pullRequestStateToString(it)
            assertEquals(it.name, result)
        }
    }
}