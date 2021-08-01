package net.onefivefour.android.bitpot.data.repositories.mediators

import androidx.paging.*
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.database.RepositoriesDao
import net.onefivefour.android.bitpot.data.database.pagingkeys.RepoPagingKeysDao
import net.onefivefour.android.bitpot.data.model.converter.RepositoryConverter
import net.onefivefour.android.bitpot.data.model.paging.RepoPagingKey
import net.onefivefour.android.bitpot.network.apifields.RepositoriesApiFields
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import net.onefivefour.android.bitpot.data.model.Repository as AppRepository
import net.onefivefour.android.bitpot.network.model.repositories.Repository as NetworkRepository

/**
 * This [RemoteMediator] is orchestrating the caching, appending and loading of the list
 * of [AppRepository] from the database and network. If the UI reached the end of the database entries,
 * the [PagingDataAdapter] will call the [load] method to fetch the next page of items from the network
 * 
 * The network response is then converted into app domain objects which again are cached in the database.
 */
@ExperimentalPagingApi
class RepositoriesRemoteMediator : RemoteMediator<Int, AppRepository>(), KoinComponent {

    private val repositoriesDao: RepositoriesDao by inject()

    private val repositoryConverter: RepositoryConverter by inject()

    private val repoPagingKeysDao: RepoPagingKeysDao by inject()

    private val api = BitbucketService.get(RepositoriesApiFields())

    override suspend fun load(loadType: LoadType, state: PagingState<Int, AppRepository>): MediatorResult {

        // This identifier is identifying the paging keys used to fetch the next batch of items.
        val workspaceUuid = BitpotData.getSelectedWorkspaceUuid()

        if (workspaceUuid.isNullOrEmpty()) {
            Timber.d("++ aborted, because workspaceUuid is null or empty")
            return MediatorResult.Error(IllegalArgumentException("workspaceUuid may not be null or empty."))
        }

        try {
            // Depending on the loadType, we get the pageKey for the API call
            val pageKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = repoPagingKeysDao.getPagingKey(workspaceUuid)
                    when (remoteKey == null) {
                        true -> return MediatorResult.Success(endOfPaginationReached = true)
                        else -> remoteKey
                    }
                }
            }

            // the actual API call to get the repositories of the currently selected workspace.
            val items = api.getRepositories(
                workspaceUuid = workspaceUuid,
                page = pageKey,
                pageLength = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )

            // if a refresh was made, delete all cached items from the database
            if (loadType == LoadType.REFRESH) {
                repositoriesDao.delete(workspaceUuid)
                repoPagingKeysDao.deletePagingKey(workspaceUuid)
            }

            // convert network response to data classes
            val convertedRepositories = convertRepositories(items.values)
            repositoriesDao.insert(convertedRepositories)
            repositoryConverter.computeRepositoryAvatars(convertedRepositories)

            val nextPageKey = items.page + 1
            if (items.values.isNotEmpty()) {
                // insert paging key into database
                val pagingKey = RepoPagingKey(workspaceUuid, nextPageKey)
                repoPagingKeysDao.insert(pagingKey)
            }
            
            
            // TODO start Worker to load and save the pipeline status of each repository

            return MediatorResult.Success(endOfPaginationReached = items.values.isEmpty())
            
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    /**
     * @return a list of [AppRepository] which were converted from the network response.
     */
    private fun convertRepositories(networkRepos: List<NetworkRepository>?): List<AppRepository> {
        val items = networkRepos ?: return emptyList()
        return items.map { repositoryConverter.toAppModel(it) }
    }
}
