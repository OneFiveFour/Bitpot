package net.onefivefour.android.bitpot.data.repositories.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import net.onefivefour.android.bitpot.data.common.FileHelper
import net.onefivefour.android.bitpot.data.database.DownloadsDao
import net.onefivefour.android.bitpot.data.database.pagingkeys.DownloadPagingKeysDao
import net.onefivefour.android.bitpot.data.model.converter.DownloadConverter
import net.onefivefour.android.bitpot.data.model.paging.DownloadPagingKey
import net.onefivefour.android.bitpot.network.apifields.DownloadsApiFields
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.temporal.ChronoUnit
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import net.onefivefour.android.bitpot.data.model.Download as AppDownload
import net.onefivefour.android.bitpot.network.model.downloads.Download as NetworkDownload

/**
 * This is part of the Jetpack Paging library version 3.0
 *
 * The RemoteMediators responsibility is to fetch the next batch of items from the network, convert them into our app domain models
 * and store them in the database. By doing so, the database becomes our single spot of truth.
 *
 * Together with the PagingSource which is created by Room, the ViewModels can then build the Pager instances.
 */
@ExperimentalPagingApi
class DownloadsRemoteMediator(private val repositoryUuid: String) : RemoteMediator<Int, AppDownload>(), KoinComponent {

    private val downloadsDao: DownloadsDao by inject()

    private val downloadPagingKeysDao: DownloadPagingKeysDao by inject()

    private val api = BitbucketService.get(DownloadsApiFields())

    override suspend fun load(loadType: LoadType, state: PagingState<Int, AppDownload>): MediatorResult {

        if (repositoryUuid.isEmpty()) {
            Timber.d("++ aborted, because repositoryUuid is empty")
            return MediatorResult.Error(IllegalArgumentException("repositoryUuid may not be empty."))
        }

        // This identifier is identifying the paging keys used to fetch the next batch of items.
        val downloadIdentifier = repositoryUuid

        try {
            val pageKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = downloadPagingKeysDao.getPagingKey(downloadIdentifier)
                    when (remoteKey == null) {
                        true -> return MediatorResult.Success(endOfPaginationReached = true)
                        else -> remoteKey
                    }
                }
            }

            // the actual API call to get the pull requests of the current repository
            val items = api.getDownloads(
                pageId = pageKey,
                pageLength = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )

            // if a refresh was made, delete all cached items from the database
            if (loadType == LoadType.REFRESH) {
                downloadsDao.delete(repositoryUuid)
                downloadPagingKeysDao.deletePagingKey(downloadIdentifier)
            }

            // convert network response and put it into the database
            val convertedDownloads = convertNetworkResponse(items.values)
            downloadsDao.insert(convertedDownloads)

            // extract key for the next page
            val nextPageKey = items.page + 1

            // insert paging key into database
            val pagingKey = DownloadPagingKey(downloadIdentifier, nextPageKey)
            downloadPagingKeysDao.insertOrReplace(pagingKey)

            return MediatorResult.Success(endOfPaginationReached = items.values.isEmpty())

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    /**
     * Convert the given network model into a list of app model [AppDownload]s.
     */
    private suspend fun convertNetworkResponse(networkSources: List<NetworkDownload>): List<AppDownload> {
        return networkSources.map { networkSource ->
            val download = DownloadConverter().toAppModel(networkSource)
            download.repoUuid = repositoryUuid
            download.downloadProgress = computeDownloadProgress(download)
            download            
        }
    }

    private suspend fun computeDownloadProgress(download: net.onefivefour.android.bitpot.data.model.Download): Float {
        val downloadExists = FileHelper.downloadExists(download)
        return if (downloadExists) {

            // file was already downloaded once. check for server changes on the file
            val existingItemCreatedOn = downloadsDao.get(download.id)?.createdOn?.truncatedTo(ChronoUnit.SECONDS)
            val newItemCreatedOn = download.createdOn.truncatedTo(ChronoUnit.SECONDS)
            val downloadChanged = existingItemCreatedOn == null || newItemCreatedOn.isAfter(existingItemCreatedOn)

            if (downloadChanged) {
                // new file available on server -> delete existing download, reset progress to zero
                FileHelper.deleteDownload(download)
                0f
            } else {
                // file did not change since last download -> set progress to 100%
                100f
            }

        } else {
            // file was never downloaded before -> progress set to zero
            0f
        }
    }

}
