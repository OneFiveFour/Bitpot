package net.onefivefour.android.bitpot.data.repositories.mediators

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import net.onefivefour.android.bitpot.data.database.SourcesDao
import net.onefivefour.android.bitpot.data.database.pagingkeys.SourcePagingKeysDao
import net.onefivefour.android.bitpot.data.model.converter.SourceConverter
import net.onefivefour.android.bitpot.data.model.paging.SourcePagingKey
import net.onefivefour.android.bitpot.network.apifields.SourcesApiFields
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import net.onefivefour.android.bitpot.data.model.Source as AppSource
import net.onefivefour.android.bitpot.network.model.sources.Source as NetworkSource

/**
 * This is part of the Jetpack Paging libary version 3.0
 *
 * The RemoteMediators responsibility is to fetch the next batch of items from the network, convert them into our app domain models
 * and store them in the database. By doing so, the database becomes our single spot of truth.
 *
 * Together with the PagingSource which is created by Room, the ViewModels can build the Pager instances.
 */
@ExperimentalPagingApi
class SourcesRemoteMediator(private val repositoryUuid: String, private val refName: String, val path: String) : RemoteMediator<Int, AppSource>(), KoinComponent {

    private val sourcesDao: SourcesDao by inject()

    private val sourcePagingKeysDao: SourcePagingKeysDao by inject()

    private val api = BitbucketService.get(SourcesApiFields())

    override suspend fun load(loadType: LoadType, state: PagingState<Int, AppSource>): MediatorResult {

        if (repositoryUuid.isEmpty() || refName.isEmpty()) {
            Timber.d("++ aborted, because repositoryUuid or refName is empty")
            return MediatorResult.Error(IllegalArgumentException("repositoryUuid and refName may not be empty."))
        }

        // This identifier is identifying the paging keys used to fetch the next batch of items.
        val sourceIdentifier = "$repositoryUuid/$refName/$path"

        try {
            val pageKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = sourcePagingKeysDao.getPagingKey(sourceIdentifier)
                    when (remoteKey.isNullOrEmpty()) {
                        true -> return MediatorResult.Success(endOfPaginationReached = true)
                        else -> remoteKey
                    }
                }
            }

            // the actual API call to get the sources of the current refHash/path combination
            val items = api.getSources(
                refName = refName,
                path = path,
                pageId = pageKey,
                pageLength = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )

            // if a refresh was made, delete all cached items from the database
            if (loadType == LoadType.REFRESH) {
                sourcesDao.delete(repositoryUuid)
                sourcePagingKeysDao.deletePagingKey(sourceIdentifier)
            }

            // convert network response and put it into the database
            val convertedSources = convertNetworkResponse(items.values)
            sourcesDao.insert(convertedSources)

            // extract key for the next page
            val nextPageKey = extractNextPageKey(items.nextPageUrl)

            if (nextPageKey.isNotEmpty()) {
                // insert paging key into database
                val pagingKey = SourcePagingKey(sourceIdentifier, nextPageKey)
                sourcePagingKeysDao.insertOrReplace(pagingKey)
            }

            return MediatorResult.Success(endOfPaginationReached = nextPageKey.isEmpty())

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    /**
     * The response of the API call has a String as nextPageId which represents an URL to the next page.
     * This URL has a GET parameter "page" that leads to the next page.
     * We extract this url parameter and return it. Otherwise an empty String.
     */
    private fun extractNextPageKey(nextPageUrl: String?): String {
        if (nextPageUrl.isNullOrEmpty()) return ""
        val uri = Uri.parse(nextPageUrl)
        return uri.getQueryParameter("page") ?: ""
    }

    /**
     * Convert the given network model into a list of app model [AppSource]s.
     */
    private fun convertNetworkResponse(networkSources: List<NetworkSource>): List<AppSource> {
        return networkSources.map { networkSource ->
            SourceConverter().toAppModel(networkSource).also {
                it.repoUuid = repositoryUuid
                it.refName = refName
            }
        }
    }

}
