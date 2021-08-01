package net.onefivefour.android.bitpot.screens.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.database.DownloadsDao
import net.onefivefour.android.bitpot.data.model.Download
import net.onefivefour.android.bitpot.data.repositories.mediators.DownloadsRemoteMediator
import timber.log.Timber


/**
 * This ViewModel is responsible for loading the list of [Download]s whenever a
 * repository is selected. This list is coming from the database.
 */
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class DownloadsViewModel(private val downloadsDao: DownloadsDao) : ViewModel() {

    private val selectedRepositoryUuid = MutableStateFlow("")

    fun getDownloads() = selectedRepositoryUuid.flatMapLatest { repositoryUuid ->
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = DownloadsRemoteMediator(repositoryUuid),
            pagingSourceFactory = { downloadsDao.getByRepoUuid(repositoryUuid) }
        )
            .flow
            .cachedIn(viewModelScope)
    }

    fun setSelectedRepositoryUuid(repositoryUuid: String) {
        selectedRepositoryUuid.value = repositoryUuid
    }

    fun download(downloadId: String, downloadUrl: String) = BitpotData.download(downloadId, downloadUrl)
}