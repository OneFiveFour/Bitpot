package net.onefivefour.android.bitpot.data.repositories.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import net.onefivefour.android.bitpot.data.database.PullRequestsDao
import net.onefivefour.android.bitpot.data.database.pagingkeys.PullRequestPagingKeysDao
import net.onefivefour.android.bitpot.data.model.converter.PullRequestConverter
import net.onefivefour.android.bitpot.data.model.paging.PullRequestPagingKey
import net.onefivefour.android.bitpot.network.apifields.PullRequestsApiFields
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import net.onefivefour.android.bitpot.data.model.PullRequest as AppPullRequest
import net.onefivefour.android.bitpot.network.model.pullrequests.PullRequest as NetworkPullRequest

/**
 * This is part of the Jetpack Paging library version 3.0
 *
 * The RemoteMediators responsibility is to fetch the next batch of items from the network, convert them into our app domain models
 * and store them in the database. By doing so, the database becomes our single spot of truth.
 *
 * Together with the PagingSource which is created by Room, the ViewModels can then build the Pager instances.
 */
@ExperimentalPagingApi
class PullRequestsRemoteMediator(private val repositoryUuid: String) : RemoteMediator<Int, AppPullRequest>(), KoinComponent {

    private val pullRequestsDao: PullRequestsDao by inject()

    private val pullRequestPagingKeysDao: PullRequestPagingKeysDao by inject()

    private val api = BitbucketService.get(PullRequestsApiFields())

    override suspend fun load(loadType: LoadType, state: PagingState<Int, AppPullRequest>): MediatorResult {

        if (repositoryUuid.isEmpty()) {
            Timber.d("++ aborted, because repositoryUuid is empty")
            return MediatorResult.Error(IllegalArgumentException("repositoryUuid may not be empty."))
        }

        // This identifier is identifying the paging keys used to fetch the next batch of items.
        val pullRequestIdentifier = repositoryUuid

        try {
            val pageKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = pullRequestPagingKeysDao.getPagingKey(pullRequestIdentifier)
                    when (remoteKey == null) {
                        true -> return MediatorResult.Success(endOfPaginationReached = true)
                        else -> remoteKey
                    }
                }
            }

            // the actual API call to get the pull requests of the current repository
            val items = api.getPullRequests(
                pageId = pageKey,
                pageLength = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )

            // if a refresh was made, delete all cached items from the database
            if (loadType == LoadType.REFRESH) {
                pullRequestsDao.delete(repositoryUuid)
                pullRequestPagingKeysDao.deletePagingKey(pullRequestIdentifier)
            }

            // convert network response and put it into the database
            val convertedPullRequests = convertNetworkResponse(items.values)
            pullRequestsDao.insert(convertedPullRequests)

            // extract key for the next page
            val nextPageKey = items.page + 1

            // insert paging key into database
            val pagingKey = PullRequestPagingKey(pullRequestIdentifier, nextPageKey)
            pullRequestPagingKeysDao.insertOrReplace(pagingKey)

            return MediatorResult.Success(endOfPaginationReached = items.values.isEmpty())

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    /**
     * Convert the given network model into a list of app model [AppPullRequest]s.
     */
    private fun convertNetworkResponse(networkSources: List<NetworkPullRequest>): List<AppPullRequest> {
        return networkSources.map { networkSource ->
            PullRequestConverter().toAppModel(networkSource)
        }
    }

}
