package net.onefivefour.android.bitpot.data.repositories.mediators

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import net.onefivefour.android.bitpot.data.database.RefsDao
import net.onefivefour.android.bitpot.data.database.pagingkeys.RefPagingKeysDao
import net.onefivefour.android.bitpot.data.model.converter.RefConverter
import net.onefivefour.android.bitpot.data.model.paging.RefPagingKey
import net.onefivefour.android.bitpot.network.apifields.RefsApiFields
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import net.onefivefour.android.bitpot.data.model.Ref as AppRef
import net.onefivefour.android.bitpot.data.model.RefType as AppRefType
import net.onefivefour.android.bitpot.network.model.refs.Ref as NetworkRef
import net.onefivefour.android.bitpot.network.model.refs.RefType as NetworkRefType

/**
 * This is part of the Jetpack Paging library version 3.0
 * 
 * The RemoteMediators responsibility is to fetch the next batch of items from the network, convert them into our app domain models
 * and store them in the database. By doing so, the database becomes our single spot of truth.
 * 
 * Together with the PagingSource which is created by Room, the ViewModels can then build the Pager instances.
 */
@ExperimentalPagingApi
class RefsRemoteMediator(private val repositoryUuid: String, private val refType: AppRefType) : RemoteMediator<Int, AppRef>(), KoinComponent {

    private val refsDao: RefsDao by inject()

    private val refPagingKeysDao: RefPagingKeysDao by inject()

    private val api = BitbucketService.get(RefsApiFields())

    @Suppress("ComplexMethod")
    override suspend fun load(loadType: LoadType, state: PagingState<Int, AppRef>): MediatorResult {

        if (repositoryUuid.isEmpty()) {
            Timber.d("++ aborted, because repositoryUuid is empty")
            return MediatorResult.Error(IllegalArgumentException("repositoryUuid may not be empty."))
        }

        // This identifier is identifying the paging keys used to fetch the next batch of items.
        val refIdentifier = "$repositoryUuid/${refType.name}"

        try {
            // Depending on the loadType, we get the pageKey for the API call
            val pageKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = refPagingKeysDao.getPagingKey(refIdentifier)
                    when (remoteKey.isNullOrEmpty()) {
                        true -> return MediatorResult.Success(endOfPaginationReached = true)
                        else -> remoteKey
                    }
                }
            }

            // convert the API params
            val networkRefType = when (refType) {
                AppRefType.BRANCH -> NetworkRefType.BRANCH
                AppRefType.TAG -> NetworkRefType.TAG
                AppRefType.ALL -> NetworkRefType.ALL
            }

            // the actual API call to get the refs of the current repository/refType combination
            val items = api.getRefs(
                networkRefType.apiValue,
                pageId = pageKey,
                pageLength = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )

            // if a refresh was made, delete all cached items from the database
            if (loadType == LoadType.REFRESH) {
                refsDao.delete(repositoryUuid)
                refPagingKeysDao.deletePagingKey(refIdentifier)
            }

            // convert network response to data classes
            val convertedRefs = convertRefValues(items.values)
            refsDao.insert(convertedRefs)

            // extract key for the next page
            val nextPageKey = extractNextPageKey(items.nextPageUrl)

            if (nextPageKey.isNotEmpty()) {
                // insert paging key into database
                val pagingKey = RefPagingKey(refIdentifier, nextPageKey)
                refPagingKeysDao.insert(pagingKey)
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
     * Convert the given network model into a list of app model [AppRef]s.
     */
    private fun convertRefValues(networkRefs: List<NetworkRef>): List<AppRef> {
        return networkRefs.map { RefConverter().toAppModel(it) }
    }

}
