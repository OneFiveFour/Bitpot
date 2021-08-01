package net.onefivefour.android.bitpot.screens.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.onefivefour.android.bitpot.data.model.Repository
import net.onefivefour.android.bitpot.data.repositories.RepoRepository

/**
 * This ViewModel loads the list of Bitbucket Repositories of the user.
 * It uses the Android paging library version 3 with a database for caching the repositories.
 */
@ExperimentalPagingApi
class RepositoriesViewModel(private val repoRepository: RepoRepository) : ViewModel() {

    init {
        getRepositories()
    }

    private lateinit var _repositories: Flow<PagingData<Repository>>
    val repositories: Flow<PagingData<Repository>>
        get() = _repositories

    /**
     * Load all repositories of the selected workspace.
     * 
     * @return a [Flow] of all repositories as [PagingData]. If needed, [AdBanner]s are 
     * added here as list separators.
     */
    private fun getRepositories() = viewModelScope.launch {
        _repositories = repoRepository.getRepositories()
            .cachedIn(viewModelScope)
    }

}
