package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.meta.dummies.PullRequestDummies
import net.onefivefour.android.bitpot.data.model.PullRequestState
import net.onefivefour.android.bitpot.data.model.converter.PullRequestConverter
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class PullRequestConverterTest {

    private lateinit var sut: PullRequestConverter

    private val pullRequestDummy = PullRequestDummies.getNetworkPullRequest()

    @Before
    fun setup() {
        sut = PullRequestConverter()
    }

    @Test
    fun convert_simplePullRequest_returnsConvertedPullRequest() {
        val result = sut.toAppModel(pullRequestDummy)

        assertEquals(1, result.id)
        assertEquals("net.onefivefour.android.bitpot.network.model.repositories.Branch.name", result.destinationBranchName)
        assertEquals("net.onefivefour.android.bitpot.network.model.repositories.Branch.name", result.sourceBranchName)
        assertEquals(1572278556L, result.lastUpdated.epochSecond)
        assertEquals("Repository.uuid", result.repoUuid)
        assertThat(result.state, instanceOf(PullRequestState.OPEN::class.java))

    }


}