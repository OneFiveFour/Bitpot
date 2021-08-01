package net.onefivefour.android.bitpot.data.repositories

import androidx.lifecycle.LiveData
import net.onefivefour.android.bitpot.data.common.NetworkDataConversion
import net.onefivefour.android.bitpot.data.database.PullRequestsDao
import net.onefivefour.android.bitpot.data.model.PostMerge
import net.onefivefour.android.bitpot.data.model.PullRequest
import net.onefivefour.android.bitpot.data.model.PullRequestDetails
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.data.model.converter.ApprovalParticipantConverter
import net.onefivefour.android.bitpot.data.model.converter.MergeConverter
import net.onefivefour.android.bitpot.data.model.converter.PullRequestDetailConverter
import net.onefivefour.android.bitpot.data.model.converter.UnitConverter
import net.onefivefour.android.bitpot.network.apifields.ParticipantApiFields
import net.onefivefour.android.bitpot.network.apifields.PullRequestApiFields
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import net.onefivefour.android.bitpot.data.model.Participant as AppParticipant
import net.onefivefour.android.bitpot.network.model.approve.Participant as NetworkParticipant
import net.onefivefour.android.bitpot.network.model.pullrequest.PullRequest as NetworkPullRequest


/**
 * This repository allows access to all detailed information of a pull request.
 */
class PullRequestRepository(private val pullRequestDao: PullRequestsDao) : KoinComponent {


    /**
     * @return the [PullRequestDetails] of the given id wrapped in a [Resource] to get network states.
     * Served as observable LiveData instance.
     */
    fun getPullRequestDetails(pullRequestId: Int): LiveData<Resource<PullRequestDetails>> {
        return object : NetworkDataConversion<NetworkPullRequest, PullRequestDetails>() {
            override fun getNetworkCall() = BitbucketService.get(PullRequestApiFields()).getPullRequest(pullRequestId)
            override fun getConverter() = PullRequestDetailConverter()
        }.get()
    }

    /**
     * @return the participant object of the approved pull request.
     * The returned participant contains the updated values for itself.
     */
    fun approve(pullRequestId: Int): LiveData<Resource<AppParticipant>> {
        return object : NetworkDataConversion<NetworkParticipant, AppParticipant>() {
            override fun getNetworkCall() = BitbucketService.get(ParticipantApiFields()).postApproval(pullRequestId)
            override fun getConverter() = ApprovalParticipantConverter()
        }.get()
    }

    /**
     * @return a [Unit]. This call returns a 204 HTTP code (empty response).
     * We wrap this response in the usual [Resource] for the UI.
     */
    fun unapprove(pullRequestId: Int): LiveData<Resource<Unit>> {
        return object : NetworkDataConversion<Unit, Unit>() {
            override fun getNetworkCall() = BitbucketService.get().deleteApproval(pullRequestId)
            override fun getConverter() = UnitConverter()
        }.get()
    }

    /**
     * @return the [PullRequestDetails] of the given id after merging the PR. All wrapped in a [Resource] to get network states.
     * Served as observable LiveData instance.
     */
    fun merge(pullRequestId: Int, merge: PostMerge): LiveData<Resource<PullRequestDetails>> {
        val postMerge = MergeConverter().toNetworkModel(merge)
        return object : NetworkDataConversion<NetworkPullRequest, PullRequestDetails>() {
            override fun getNetworkCall() = BitbucketService.get(PullRequestApiFields()).postMerge(pullRequestId, postMerge)
            override fun getConverter() = PullRequestDetailConverter()
        }.get()
    }

    /**
     * @return the basic information of the given [pullRequestId]. Used to 
     * display the header bar in the pull request details screen.
     */
    fun getPullRequest(pullRequestId: Int?): LiveData<PullRequest> {
        return pullRequestDao.getById(pullRequestId)  
    } 

}