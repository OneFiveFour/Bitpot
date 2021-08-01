package net.onefivefour.android.bitpot.screens.repository

import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.*
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.analytics.AnalyticsEvent
import net.onefivefour.android.bitpot.analytics.EventTracker
import net.onefivefour.android.bitpot.analytics.model.ToggleAction
import net.onefivefour.android.bitpot.data.common.HttpStatus
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.data.model.ResourceStatus
import net.onefivefour.android.bitpot.data.model.WebHook
import net.onefivefour.android.bitpot.data.model.WebHookEvent
import net.onefivefour.android.bitpot.data.repositories.WebHooksRepository

/**
 * This ViewModel is used to handle the web hook attached to a repository.
 * WebHooks are used in this app to trigger push notifications for all possible Events defined in [WebHookEvent]
 *
 * Each repository can have up to 1 WebHook. This WebHook defines all actions that the user wants to
 * receive notifications for. If notifications are turned off, the WebHook gets deleted.
 *
 * This ViewModel allows and takes care of all CRUD operations on a web hook.
 */
class WebHookViewModel(
    webHooksRepository: WebHooksRepository, 
    private val eventTracker: EventTracker,
    workspaceUuid: String,
    repositoryUuid: String

) : ViewModel() {

    /**
     * Post data to this LiveData to trigger UI Events.
     */
    private val uiEvents = MutableLiveData<UiEvent>()

    /**
     * Post data to this LiveData to create a new web hook with the given events activated.
     */
    private val createWebHook = MutableLiveData<List<WebHookEvent>>()

    /**
     * Post data to this LiveData to update the existing set of active events in the web hook.
     */
    private val updateWebHookEvents = MutableLiveData<List<WebHookEvent>>()

    /**
     * Post the Uuid of a WebHook to this LiveData to trigger the deletion of it.
     */
    private val deleteWebHook = MutableLiveData<String>()

    /**
     * Fetches the web hook of the current repository as soon as this LiveData gets observed.
     */
    private val getWebHookCall = webHooksRepository.getWebHook(workspaceUuid, repositoryUuid)

    /**
     * Whenever a new set of [WebHookEvent]s is posted to [createWebHook],
     * the repository is called to create a new WebHook with these events.
     */
    private val createWebHookCall = Transformations.switchMap(createWebHook) { events ->
        webHooksRepository.createWebHook(events)
    }

    /**
     * Whenever a new set of [WebHookEvent]s is posted to [updateWebHookEvents],
     * the repository is called to update the current WebHook with these events.
     */
    private val updateWebHookCall: LiveData<Resource<WebHook>> = Transformations.switchMap(updateWebHookEvents) { events ->
        val currentWebHook = webHook.value ?: return@switchMap MutableLiveData<Resource<WebHook>>()
        webHooksRepository.updateWebHook(currentWebHook, events)
    }

    /**
     * If notifications are turned off by the user (i.e. all WebHookEvents are deactivated), the WebHook gets deleted here.
     */
    private val deleteWebHookCall = Transformations.switchMap(deleteWebHook) { uuid ->
        webHooksRepository.deleteWebHook(uuid)
    }

    /**
     * Observe this LiveData to receive updates of the WebHook of the current repository.
     */
    fun getWebHook(): LiveData<WebHook> = webHook


    /**
     * This is the main LiveData instance of this class.
     *
     * Whenever a new value is posted to any of its sources, the current web hook is updated accordingly.
     * Also [UiEvent]s are triggered when needed.
     */
    private val webHook = MediatorLiveData<WebHook>().apply {
        addSource(getWebHookCall) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> resource.data
                ResourceStatus.ERROR -> return@addSource
                ResourceStatus.LOADING -> return@addSource
            }
        }
        addSource(createWebHookCall) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> resource.data
                ResourceStatus.ERROR -> {
                    when (resource.httpCode) {
                        HttpStatus.FORBIDDEN.code -> uiEvents.postValue(UiEvent.IsNotAdmin)
                        else -> uiEvents.postValue(UiEvent.Error(resource.message))
                    }
                    return@addSource
                }
                ResourceStatus.LOADING -> return@addSource
            }
        }
        addSource(updateWebHookCall) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> resource.data
                ResourceStatus.ERROR -> {
                    when (resource.httpCode) {
                        HttpStatus.FORBIDDEN.code -> uiEvents.postValue(UiEvent.IsNotAdmin)
                        else -> uiEvents.postValue(UiEvent.Error(resource.message))
                    }
                    return@addSource
                }
                ResourceStatus.LOADING -> return@addSource
            }
        }
        addSource(deleteWebHookCall) { resource ->
            value = when (resource.resourceStatus) {
                ResourceStatus.SUCCESS -> null
                ResourceStatus.ERROR -> return@addSource
                ResourceStatus.LOADING -> return@addSource
            }
        }
    }


    /**
     * Observe this LiveData to get updates of the loading state regarding the WebHook
     */
    fun isLoading(): LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(getWebHookCall) { value = getLoadingStatus() }
        addSource(createWebHookCall) { value = getLoadingStatus() }
        addSource(updateWebHookCall) { value = getLoadingStatus() }
        addSource(deleteWebHookCall) { value = getLoadingStatus() }
    }

    /**
     * Taking all possible network calls into consideration to calculate
     * the current loading state.
     */
    private fun getLoadingStatus(): Boolean {
        return getWebHookCall.value?.resourceStatus == ResourceStatus.LOADING ||
                createWebHookCall.value?.resourceStatus == ResourceStatus.LOADING ||
                updateWebHookCall.value?.resourceStatus == ResourceStatus.LOADING ||
                deleteWebHookCall.value?.resourceStatus == ResourceStatus.LOADING
    }

    /**
     * This method is called via data binding by the UI switches.
     * Each switch de-/activates its WebHookEvent.
     */
    fun toggleWebHookEvent(view: View) {
        val switch = view as SwitchCompat
        val toggleAction = if (switch.isChecked) ToggleAction.ON else ToggleAction.OFF

        when (switch.id) {
            R.id.sw_pipeline_updates -> {
                eventTracker.sendEvent(AnalyticsEvent.TogglePipelineNotifications(toggleAction))
                updateWebHook(WebHookEvent.PIPELINE_UPDATES, switch.isChecked)
            }
            R.id.sw_pull_request_updates -> {
                eventTracker.sendEvent(AnalyticsEvent.TogglePullRequestNotifications(toggleAction))
                updateWebHook(WebHookEvent.PULL_REQUEST_UPDATES, switch.isChecked)
            }
        }
    }

    /**
     * Call this method to trigger the correct action for de-/activating the given webHookEvent:
     *
     * * creating a new WebHook
     * * updating the current WebHook
     * * deleting the current WebHook
     */
    private fun updateWebHook(webHookEvent: WebHookEvent, isActive: Boolean) {
        val currentWebHook = webHook.value
        val currentEvents = currentWebHook?.events?.toTypedArray() ?: emptyArray()

        // create the updated list of events
        val newEvents = when {
            isActive -> currentEvents.plus(webHookEvent).distinct()
            else -> currentEvents.filter { it != webHookEvent }
        }

        // depending on the variables, decide whether to create, update or delete the web hook
        when {
            currentWebHook == null -> createWebHook.postValue(newEvents)
            newEvents.isEmpty() -> deleteWebHook.postValue(currentWebHook.uuid)
            else -> updateWebHookEvents.postValue(newEvents)
        }

    }

    /**
     * Observe this LiveData to get notified about UI relevant events.
     */
    fun getUiEvents(): LiveData<UiEvent> = uiEvents

    override fun onCleared() {
        super.onCleared()
        webHook.postValue(null)
    }

    /**
     * All UI events that can happen while handling web hooks.
     */
    sealed class UiEvent {

        /**
         * Admin permission needed to access WebHooks.
         */
        object IsNotAdmin : UiEvent()

        /**
         * An error occurred.
         */
        class Error(val message: String?) : UiEvent()
    }
}
