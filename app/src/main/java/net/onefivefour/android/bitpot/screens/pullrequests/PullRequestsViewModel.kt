package net.onefivefour.android.bitpot.screens.pullrequests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import net.onefivefour.android.bitpot.data.database.PullRequestsDao
import net.onefivefour.android.bitpot.data.model.PullRequest
import net.onefivefour.android.bitpot.data.repositories.mediators.PullRequestsRemoteMediator
import timber.log.Timber

/**
 * This ViewModel is responsible for loading the list of [PullRequest]s whenever a
 * repository is selected. This list is coming from the database.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PullRequestsViewModel(private val pullRequestsDao: PullRequestsDao) : ViewModel() {

    private val selectedRepositoryUuid = MutableStateFlow("")

    fun getPullRequests() = selectedRepositoryUuid.flatMapLatest { repositoryUuid ->

        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = PullRequestsRemoteMediator(repositoryUuid),
            pagingSourceFactory = { pullRequestsDao.get(repositoryUuid) }
        )
            .flow
            .cachedIn(viewModelScope)
    }

    fun setSelectedRepositoryUuid(repositoryUuid: String) {
        selectedRepositoryUuid.value = repositoryUuid
    }
}