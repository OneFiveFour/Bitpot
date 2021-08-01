package net.onefivefour.android.bitpot.screens.pullrequest

import androidx.lifecycle.*
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.model.*
import net.onefivefour.android.bitpot.data.repositories.PullRequestRepository


/**
 * Taking the id of a pull request, this ViewModel provides detailed information about this pull request.
 *
 * This ViewModel can also be used to approve, unapprove and merge such a pull request.
 */
class PullRequestViewModel(private val pullRequestRepository: PullRequestRepository) : ViewModel() {


    /**
     * Observe this LiveData to get updates that may be of interest for the UI
     */
    fun getPullRequestUiEvents(): LiveData<UiEvent> = MediatorLiveData<UiEvent>().apply {
        addSource(mergeCall) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> UiEvent.MergeSuccess
                ResourceStatus.ERROR -> UiEvent.MergeError(resource.message)
                ResourceStatus.LOADING -> UiEvent.MergeLoading
            }
        }
    }

    /**
     * MutableLiveData to trigger the approval or unapproval of this PR.
     * Post a value of true or false to set the un-/approval process in motion.
     */
    private val approvalTrigger = MutableLiveData<Boolean>()

    /**
     * MutableLiveData to trigger the merging of this PR
     * Post a value of [PostMerge] to merge this PR into the destination branch
     */
    private val mergeTrigger = MutableLiveData<PostMerge>()

    /**
     * The id of the current pull request.
     */
    private val pullRequestId = MediatorLiveData<Int>()

    /**
     * The API call for the pull request details.
     * This LiveData returns a Resource which can be transformed later on to create the correct pullRequestDetails
     */
    private val pullRequestDetailsCall = Transformations.switchMap(pullRequestId) { id ->
        pullRequestRepository.getPullRequestDetails(id)
    }

    /**
     * The API call for approving the current pull request.
     * This LiveData returns a Resource which can be transformed later on to create the correct pullRequestDetails
     */
    private val approvalCall = Transformations.switchMap(approvalTrigger) { shouldApprove ->
        if (shouldApprove == null || !shouldApprove) return@switchMap MutableLiveData<Resource<Participant>>()
        val pullRequestId = pullRequestId.value ?: return@switchMap MutableLiveData<Resource<Participant>>()
        pullRequestRepository.approve(pullRequestId)
    }

    /**
     * The API call for removing the approval of the current pull request.
     * This LiveData returns a Resource which can be transformed later on to create the correct pullRequestDetails
     */
    private val unapprovalCall = Transformations.switchMap(approvalTrigger) { shouldApprove ->
        if (shouldApprove == null || shouldApprove) return@switchMap MutableLiveData<Resource<Unit>>()
        val pullRequestId = pullRequestId.value ?: return@switchMap MutableLiveData<Resource<Unit>>()
        pullRequestRepository.unapprove(pullRequestId)
    }

    /**
     * The API call for merging the current pull request.
     * This LiveData returns a Resource which can be transformed later on to create the resulting pullRequestDetails
     */
    private val mergeCall = Transformations.switchMap(mergeTrigger) { merge ->
        if (merge == null) return@switchMap MutableLiveData<Resource<PullRequestDetails>>()
        val pullRequestId = pullRequestId.value ?: return@switchMap MutableLiveData<Resource<PullRequestDetails>>()
        pullRequestRepository.merge(pullRequestId, merge)
    }

    /**
     * The main LiveData of this ViewModel. It contains all details of the pull request.
     * It has several sources which have influence on the content of the PR details.
     */
    private val pullRequestDetails = MediatorLiveData<PullRequestDetails>().apply {

        /**
         * Listen for the result of the main API call to fetch PR details.
         */
        addSource(pullRequestDetailsCall) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> resource.data
                ResourceStatus.ERROR -> return@addSource
                ResourceStatus.LOADING -> return@addSource
            }
        }

        /**
         * Listen for the result of an approval API call.
         */
        addSource(approvalCall) { resource ->
            value = when (resource.resourceStatus) {
               ResourceStatus.SUCCESS -> updateParticipant(resource.data)
               ResourceStatus.ERROR -> return@addSource
               ResourceStatus.LOADING -> return@addSource
           }
        }

        /**
         * Listen for the result of an unapproval API call.
         */
        addSource(unapprovalCall) { resource ->
            value = when (resource.resourceStatus) {
               ResourceStatus.SUCCESS -> removeApproval()
               ResourceStatus.ERROR -> return@addSource
               ResourceStatus.LOADING -> return@addSource
           }
        }

        /**
         * Listen for the result of merging the PR
         */
        addSource(mergeCall) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> resource.data
                ResourceStatus.ERROR -> return@addSource
                ResourceStatus.LOADING -> return@addSource
            }
        }
    }

    /**
     * This LiveData extracts the approval state of the current user for this PR.
     */
    fun hasApproved() = Transformations.map(pullRequestDetails) { pullRequest ->
        // post if current user has approved the incoming pull request
        pullRequest?.participants
            ?.firstOrNull { it.accountId == BitpotData.getAccountId() }
            ?.hasApproved ?: false
    }

    /**
     * Observe this LiveData to get updates about the current merge state of this PR.
     */
    fun isMerged() = Transformations.map(pullRequestDetails) { it.state == PullRequestStatus.MERGED }

    /**
     * This LiveData keeps track of the network status of all API calls of this ViewModel
     */
    fun isLoading() = MediatorLiveData<Boolean>().apply {
        addSource(pullRequestDetailsCall) { value = it.resourceStatus == ResourceStatus.LOADING }
        addSource(approvalCall) { value = isApprovalLoading() }
        addSource(unapprovalCall) { value = isApprovalLoading() }
        addSource(mergeCall) { value = it.resourceStatus == ResourceStatus.LOADING }
    }

    private fun isApprovalLoading(): Boolean {
        return  approvalTrigger.value == false &&  unapprovalCall.value?.resourceStatus == ResourceStatus.LOADING ||
                approvalTrigger.value == true && approvalCall.value?.resourceStatus == ResourceStatus.LOADING
    }

    /**
     * Called when the current user approved a PR.
     * The newParticipant is not necessarily an already existing participant.
     * Therefore we have to either update or add it to the list of participants.
     */
    private fun updateParticipant(newParticipant: Participant?): PullRequestDetails? {
        if (newParticipant == null) return null
        val details = pullRequestDetails.value ?: return null

        // see if newParticipant is already existing
        for (curParticipant in details.participants) {
            if (newParticipant.accountId == curParticipant.accountId) {
                curParticipant.hasApproved = newParticipant.hasApproved
                return details
            }
        }

        // newParticipant was not found in existing list -> add it
        details.participants = details.participants
            .toTypedArray()
            .plus(newParticipant)
            .toList()

        return details
    }

    /**
     * Called when the current user removed his/her approval for this PR.
     * To remove an approval, the user must already be part of the list of participants.
     * Therefore, we search for him/her and set hasApproved to false.
     */
    private fun removeApproval(): PullRequestDetails?  {
        val details = pullRequestDetails.value ?: return null
        val uuid = BitpotData.getAccountId()

        details.participants.first { it.accountId == uuid }.hasApproved = false
        return details
    }

    /**
     * Observe this LiveData to get the basic information about the pull request.
     */
    val pullRequestHeader = Transformations.switchMap(pullRequestId) { pullRequestId ->
        pullRequestRepository.getPullRequest(pullRequestId)    
    }

    fun getPullRequestDetails(): LiveData<PullRequestDetails> = pullRequestDetails

    fun setPullRequestId(id: Int) = pullRequestId.postValue(id)

    fun setApproval(hasApproved: Boolean) = approvalTrigger.postValue(hasApproved)

    fun merge(merge: PostMerge) = mergeTrigger.postValue(merge)

    /**
     * All UI events that can happen regarding merging a pull request.
     */
    sealed class UiEvent {

        /**
         * Merging the PR was successful
         */
        object MergeSuccess: UiEvent()

        /**
         * There was an error while merging the PR.
         */
        class MergeError(val error: String?) : UiEvent()

        /**
         * The PR is currently being merged.
         */
        object MergeLoading: UiEvent()
    }
}
