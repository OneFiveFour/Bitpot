package net.onefivefour.android.bitpot.screens.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.model.RepositoryColors
import net.onefivefour.android.bitpot.data.model.ResourceStatus
import net.onefivefour.android.bitpot.data.repositories.RepoColorsRepository
import net.onefivefour.android.bitpot.data.repositories.RepoRepository

/**
 * This ViewModel holds all information needed "globally" within the Repository Details area.
 */
class RepositoryViewModel(
    repoRepository: RepoRepository,
    repoColorsRepository: RepoColorsRepository,
    workspaceUuid: String,
    repositoryUuid: String) : ViewModel() {

    
    private val repositoryCall = repoRepository.getRepository(workspaceUuid, repositoryUuid)

    /**
     * Observe this LiveData to get the currently selected/opened repository
     */
    val repository = Transformations.map(repositoryCall) { resource ->
        when (resource.resourceStatus) {
            ResourceStatus.SUCCESS -> {
                val repository = resource.data ?: return@map null
                // update the last opened repository to fetch only data from this repo
                // from now on. ATTENTION! This is running asynchronously to other API calls.
                // DO NOT trust that all API calls will use the correct lastRepositoryFullName
                // at all times! In fact, if there is something fishy with such an API call, it is 
                // recommended to inject the workspaceUuid and the repositoryUuid into the affected
                // ViewModel and use them to explicitly set the repositoryFullName in the API call.
                // (see WebHookViewModel for an example)
                BitpotData.setLastRepositoryFullName("${repository.workspaceUuid}/${repository.name}")
                repository
            }
            else -> null
        }
    }

    /**
     * Observe this LiveData to get all repositoryColors of the
     * currently selected/opened repository
     */
    val repositoryColors = Transformations.switchMap(repository) { repository ->
        if (repository == null) return@switchMap MutableLiveData<RepositoryColors>()
        repoColorsRepository.getRepositoryColors(repository.uuid)
    }

}