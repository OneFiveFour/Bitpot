package net.onefivefour.android.bitpot.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.common.NetworkBoundResource
import net.onefivefour.android.bitpot.data.database.RepositoriesDao
import net.onefivefour.android.bitpot.data.model.Repository
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.data.model.converter.RepositoryConverter
import net.onefivefour.android.bitpot.data.repositories.mediators.RepositoriesRemoteMediator
import net.onefivefour.android.bitpot.network.apifields.RepositoryApiFields
import net.onefivefour.android.bitpot.network.common.ApiResponse
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import net.onefivefour.android.bitpot.network.model.repositories.Repository as NetworkRepository

/**
 * The 'Repository' classes should always be the only api contact for the ui layer.
 * The ViewModels can get all their data from here and should not call any other classes directly.
 *
 * This Repository is used for paged data using the Android paging library version 3.
 * The [net.onefivefour.android.bitpot.data.database.BitpotDatabase] is used as caching mechanism to create the paged data.
 * This is done in the [RepositoriesRemoteMediator].
 *
 * A normal request from this repository goes like this:
 *
 * * The requested data is first requested from the database. The database returns a [Flow] of [PagingData] or [Repository]
 * 
 * * If the database contains no entries or we reach the end of the database, the [RepositoriesRemoteMediator] is used to fetch
 * new pages from the network. If new pages are available, they are added to the database, before being returned to the data layer.
 * 
 * To react to different loading states (refresh, append, prepend. Each of them from the source or from the mediator), the [PagingDataAdapter]
 * provides according LiveData to observe.
 *
 */
class RepoRepository(private val repositoriesDao: RepositoriesDao) : KoinComponent {

    /**
     * @return a [Flow] paged list of all repositories of the currently selected workspace.
     */
    // TODO: move Pager creation to ViewModel to make use of .cachedIn(viewModelScope)
    @ExperimentalPagingApi
    fun getRepositories(): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConfig(20),
            remoteMediator = RepositoriesRemoteMediator()
        ) {
            val workspaceUuid = BitpotData.getSelectedWorkspaceUuid() ?: ""
            repositoriesDao.getByWorkspaceUuid(workspaceUuid) 
        }.flow
    }

    /**
     * @return the currently selected [Repository] wrapped in a [Resource] to get network states.
     * Served as observable LiveData instance.
     */
    fun getRepository(workspaceUuid: String, repositoryUuid: String): LiveData<Resource<Repository>> {
        return object : NetworkBoundResource<Repository, NetworkRepository>() {
            override fun saveCallResult(item: NetworkRepository) {
                val repositoryConverter = RepositoryConverter()
                val convertedRepository = repositoryConverter.toAppModel(item)
                repositoryConverter.computeRepositoryAvatars(listOf(convertedRepository))
                repositoriesDao.insert(convertedRepository)
            }

            override fun shouldFetch(data: Repository?) = true

            override fun loadFromDb(): LiveData<Repository> = repositoriesDao.getByUuid(repositoryUuid)

            override fun createCall(): LiveData<ApiResponse<NetworkRepository>> {
                return BitbucketService.get(RepositoryApiFields()).getRepository(workspaceUuid, repositoryUuid)
            }

        }.asLiveData()
    }
}