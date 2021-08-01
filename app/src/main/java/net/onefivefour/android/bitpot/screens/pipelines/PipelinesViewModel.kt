package net.onefivefour.android.bitpot.screens.pipelines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import net.onefivefour.android.bitpot.data.database.PipelinesDao
import net.onefivefour.android.bitpot.data.model.Pipeline
import net.onefivefour.android.bitpot.data.repositories.mediators.PipelinesRemoteMediator
import timber.log.Timber

/**
 * This ViewModel is responsible for loading the list of [Pipeline]s whenever a
 * repository is selected. This list is coming from the database.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PipelinesViewModel(private val pipelinesDao: PipelinesDao) : ViewModel() {

    private val selectedRepositoryUuid = MutableStateFlow("")

    fun getPipelines() = selectedRepositoryUuid.flatMapLatest { repositoryUuid ->
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = PipelinesRemoteMediator(repositoryUuid),
            pagingSourceFactory = { pipelinesDao.get(repositoryUuid) }
        )
            .flow
            .cachedIn(viewModelScope)
    }

    fun setSelectedRepositoryUuid(repositoryUuid: String) {
        selectedRepositoryUuid.value = repositoryUuid
    }
}