package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.meta.dummies.ParticipantDummies
import net.onefivefour.android.bitpot.data.model.converter.ApprovalParticipantConverter
import net.onefivefour.android.bitpot.data.model.converter.PullRequestParticipantConverter
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ParticipantConverterTest {

    private lateinit var sut1: ApprovalParticipantConverter
    private lateinit var sut2: PullRequestParticipantConverter

    @Before
    fun setup() {
        sut1 = ApprovalParticipantConverter()
        sut2 = PullRequestParticipantConverter()
    }

    @Test
    fun toAppModel_correctNetworkApprovalParticipant_returnsCorrectAppParticipant() {
        val networkParticipant = ParticipantDummies.getApprovalParticipant()

        val result = sut1.toAppModel(networkParticipant)

        assertEquals(networkParticipant.approved, result.hasApproved)
        assertEquals(networkParticipant.user.links.avatar.href, result.avatarUrl)
        assertEquals(networkParticipant.user.accountId, result.accountId)
    }

    @Test
    fun toAppModel_correctNetworkPullRequestParticipant_returnsCorrectAppParticipant() {
        val networkParticipant = ParticipantDummies.getPullRequestParticipant()

        val result = sut2.toAppModel(networkParticipant)

        assertEquals(networkParticipant.approved, result.hasApproved)
        assertEquals(networkParticipant.user.links.avatar.href, result.avatarUrl)
        assertEquals(networkParticipant.user.accountId, result.accountId)
    }
}