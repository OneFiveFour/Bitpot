package net.onefivefour.android.bitpot.screens.sources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import net.onefivefour.android.bitpot.data.database.RefsDao
import net.onefivefour.android.bitpot.data.model.Ref
import net.onefivefour.android.bitpot.data.model.RefType
import net.onefivefour.android.bitpot.data.repositories.RefsRepository
import net.onefivefour.android.bitpot.data.repositories.mediators.RefsRemoteMediator


/**
 * This ViewModel is used to organise the list of [Ref]s of a Bitbucket repository.
 * It provides the UI with all Refs and also about the currently selected [Ref].
 */
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class RefViewModel(private val refsDao: RefsDao, private val refsRepository: RefsRepository) : ViewModel() {

    private val selectedRef = MutableLiveData<Ref>()

    private val selectedRefChanged = MutableLiveData(false)

    private val selectedRepositoryUuid = MutableStateFlow("")

    /**
     * Collect this Flow to get a list of all [Ref]s.
     * This includes branches and tags of the current repository.
     */
    val all = selectedRepositoryUuid.flatMapLatest { uuid ->
        Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            remoteMediator = RefsRemoteMediator(uuid, RefType.ALL),
            pagingSourceFactory = { refsDao.getByRepoUuid(uuid) }
        )
            .flow
            .cachedIn(viewModelScope)
    }

    /**
     * Collect this Flow to get a list of all tags or the current repository.
     * 
     * We have to make 3 different network requests instead of filtering the [all]
     * Flow, because otherwise we can only filter the list of loaded elements from 
     * the [all] list. But in reality we want to present branches/tags even if
     * they are maybe not yet loaded in [all]
     */
    val tags = selectedRepositoryUuid.flatMapLatest { uuid ->
        Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            remoteMediator = RefsRemoteMediator(uuid, RefType.TAG),
            pagingSourceFactory = { refsDao.getByRepoUuid(uuid, RefType.TAG) }
        )
            .flow
            .cachedIn(viewModelScope)
    }

    /**
     * Collect this Flow to get a list of all branches or the current repository.
     * 
     * We have to make 3 different network requests instead of filtering the [all]
     * Flow, because otherwise we can only filter the list of loaded elements from
     * the [all] list. But in reality we want to present branches/tags even if
     * they are maybe not yet loaded in [all]
     */
    val branches = selectedRepositoryUuid.flatMapLatest { uuid ->
        Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            remoteMediator = RefsRemoteMediator(uuid, RefType.BRANCH),
            pagingSourceFactory = { refsDao.getByRepoUuid(uuid, RefType.BRANCH) }
        )
            .flow
            .cachedIn(viewModelScope)
    }

    /**
     * Observe this LiveData to get the currently selected [Ref]
     */
    fun getSelectedRef(): LiveData<Ref> = selectedRef

    /**
     * Observe this LiveData to get informed when a different [Ref]
     * was selected.
     */
    fun hasSelectedRefChanged(): LiveData<Boolean> = selectedRefChanged

    /**
     * Call this to set the current repositoryUuid to this ViewModel
     * All data calls in this ViewModel will reference this repository!
     */
    fun setRepositoryUuid(repositoryUuid: String) {
        selectedRepositoryUuid.value = repositoryUuid
    }

    /**
     * Set the currently selected [Ref].
     * If the given ref is different from the prevoius one,
     * [hasSelectedRefChanged] will be triggered.
     */
    fun setSelectedRef(ref: Ref) {
        val hasChanged = selectedRef.value != ref
        if (hasChanged) {
            selectedRefChanged.value = true
            selectedRef.postValue(ref)
    
            val repoUuid = selectedRepositoryUuid.value
            refsRepository.setRefSelection(ref, repoUuid)
        }        
        
        // reset the selectedRefChanged flag in case the viewModel is used again
        selectedRefChanged.value = false
    }

}