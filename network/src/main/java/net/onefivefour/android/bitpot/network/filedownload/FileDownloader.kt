package net.onefivefour.android.bitpot.network.filedownload

import android.content.Context
import android.net.Uri
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2okhttp.OkHttpDownloader
import net.onefivefour.android.bitpot.network.SharedPrefsNetwork
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File

/**
 * Use this class to trigger the download of a file
 * found in the "Download" section of a repository.
 */
class FileDownloader(private val context: Context) {

    private val fetch: Fetch

    private var downloadsRunning = 0

    init {
        val okHttpClient = OkHttpClient.Builder().build()

        val fetchConfiguration =
            FetchConfiguration.Builder(context)
                .setDownloadConcurrentLimit(10)
                .setProgressReportingInterval(100)
                .setHttpDownloader(OkHttpDownloader(okHttpClient))
                .build()

        fetch = Fetch.getInstance(fetchConfiguration)
    }

    /**
     * Call this method to trigger downloading a file of the "Download" section of a repository.
     *
     * @param downloadId the id of the Download to download
     * @param downloadUrl the url of the download
     * @param downloadListener the [FetchListener] that should react to changes of the download progress.
     */
    fun download(downloadId: String, downloadUrl: String, destinationFile: File, downloadListener: FetchListener) {

        // delete existing files of this downloadId
        fetch.getDownloadsByTag(downloadId) { downloads -> downloads.forEach { fetch.delete(it.id) } }
        
        // add the given downloadListener
        fetch.addListener(downloadListener)
        
        // prepare download data
        val accessToken = SharedPrefsNetwork(context).getAuthState()?.accessToken ?: throw IllegalStateException("Tried downloading file without accessToken.")

        // create download request
        val request = Request(downloadUrl, Uri.fromFile(destinationFile))
        request.tag = downloadId
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL
        request.addHeader("Authorization", "Bearer $accessToken")

        // enqueue and execute download. Updates and errors are reported in the callback methods below.
        fetch.enqueue(
            request,
            {
                run {
                    downloadsRunning++
                }
            },
            { error: Error? -> Timber.d("+ Error: ${error?.name}") }
        )
    }

    fun removeDownloadListener(listener: FetchListener) {
        downloadsRunning--
        if (downloadsRunning == 0) {
            fetch.removeListener(listener)
        }
    }
}