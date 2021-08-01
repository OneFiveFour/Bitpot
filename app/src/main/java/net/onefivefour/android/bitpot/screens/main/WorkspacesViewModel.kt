package net.onefivefour.android.bitpot.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.model.ResourceStatus
import net.onefivefour.android.bitpot.data.model.Workspace
import net.onefivefour.android.bitpot.data.repositories.WorkspacesRepository

/**
 * This ViewModel provides the users workspaces as LiveData.
 */
class WorkspacesViewModel(repository: WorkspacesRepository) : ViewModel() {

    private val selectedWorkspaceUuid = MutableLiveData<String>().apply { postValue(BitpotData.getSelectedWorkspaceUuid()) }

    private val workspacesCall = repository.getWorkspaces()
    
    /**
     * Observe this to get a combined list of all user object itself.
     * This list represents all possible sources of repositories that the user has access to.
     */
    val workspaces = Transformations.map(workspacesCall) { resource ->
        when (resource.resourceStatus) {
            ResourceStatus.SUCCESS -> resource.data ?: emptyList()
            ResourceStatus.ERROR -> emptyList()
            ResourceStatus.LOADING -> emptyList()
        }
    }

    fun setSelectedWorkspaceUuid(workspaceUuid: String) {
        BitpotData.setSelectedWorkspaceUuid(workspaceUuid)
        selectedWorkspaceUuid.postValue(workspaceUuid)
    } 

    val selectedWorkspace : LiveData<Workspace> = Transformations.map(selectedWorkspaceUuid) { uuid ->
        val currentWorkspaces = workspaces.value ?: return@map null
        currentWorkspaces.firstOrNull { it.uuid == uuid }
    }
}