package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.meta.dummies.PullRequestDummies
import net.onefivefour.android.bitpot.data.model.converter.PullRequestDetailConverter
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class PullRequestDetailsConverterTest {


    private lateinit var sut: PullRequestDetailConverter

    @Before
    fun setup() {
        sut = PullRequestDetailConverter()
    }

    @Test
    fun toAppModel_correctNetworkPullRequest_returnsCorrectPullRequestDetails() {
        val networkPullRequest = PullRequestDummies.getNetworkPullRequestDetails()

        val result = sut.toAppModel(networkPullRequest)

        assertEquals(networkPullRequest.id, result.id)
        assertEquals(networkPullRequest.participants.size, result.participants.size)
        assertEquals(networkPullRequest.rendered.description.raw, result.descriptionMarkdown)
        assertEquals(networkPullRequest.state.uppercase(Locale.getDefault()), result.state.name)
        assertEquals(networkPullRequest.title, result.title)
    }
}