package net.onefivefour.android.bitpot.data.repositories.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import net.onefivefour.android.bitpot.data.database.PipelinesDao
import net.onefivefour.android.bitpot.data.database.pagingkeys.PipelinePagingKeysDao
import net.onefivefour.android.bitpot.data.model.converter.PipelineConverter
import net.onefivefour.android.bitpot.data.model.paging.PipelinePagingKey
import net.onefivefour.android.bitpot.network.apifields.PipelinesApiFields
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import net.onefivefour.android.bitpot.data.model.Pipeline as AppPipeline
import net.onefivefour.android.bitpot.network.model.pipelines.Pipeline as NetworkPipeline

/**
 * This is part of the Jetpack Paging library version 3.0
 *
 * The RemoteMediators responsibility is to fetch the next batch of items from the network, convert them into our app domain models
 * and store them in the database. By doing so, the database becomes our single spot of truth.
 *
 * Together with the PagingSource which is created by Room, the ViewModels can then build the Pager instances.
 */
@ExperimentalPagingApi
class PipelinesRemoteMediator(private val repositoryUuid: String) : RemoteMediator<Int, AppPipeline>(), KoinComponent {

    private val pipelinesDao: PipelinesDao by inject()

    private val pipelinePagingKeysDao: PipelinePagingKeysDao by inject()

    private val api = BitbucketService.get(PipelinesApiFields())

    override suspend fun load(loadType: LoadType, state: PagingState<Int, AppPipeline>): MediatorResult {

        if (repositoryUuid.isEmpty()) {
            Timber.d("++ aborted, because repositoryUuid is empty")
            return MediatorResult.Error(IllegalArgumentException("repositoryUuid may not be empty."))
        }

        // This identifier is identifying the paging keys used to fetch the next batch of items.
        val pipelineIdentifier = repositoryUuid

        try {
            val pageKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = pipelinePagingKeysDao.getPagingKey(pipelineIdentifier)
                    when (remoteKey == null) {
                        true -> return MediatorResult.Success(endOfPaginationReached = true)
                        else -> remoteKey
                    }
                }
            }

            // the actual API call to get the pull requests of the current repository
            val items = api.getPipelines(
                pageId = pageKey,
                pageLength = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )

            // if a refresh was made, delete all cached items from the database
            if (loadType == LoadType.REFRESH) {
                pipelinesDao.delete(repositoryUuid)
                pipelinePagingKeysDao.deletePagingKey(pipelineIdentifier)
            }

            // convert network response and put it into the database
            val convertedPipelines = convertNetworkResponse(items.values)
            pipelinesDao.insert(convertedPipelines)

            // extract key for the next page
            val nextPageKey = items.page + 1

            // insert paging key into database
            val pagingKey = PipelinePagingKey(pipelineIdentifier, nextPageKey)
            pipelinePagingKeysDao.insertOrReplace(pagingKey)

            return MediatorResult.Success(endOfPaginationReached = items.values.isEmpty())

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    /**
     * Convert the given network model into a list of app model [AppPipeline]s.
     */
    private fun convertNetworkResponse(networkSources: List<NetworkPipeline>): List<AppPipeline> {
        return networkSources.map { networkSource ->
            PipelineConverter().toAppModel(networkSource)
        }
    }

}
