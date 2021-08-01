package net.onefivefour.android.bitpot.network

import android.content.Context
import com.tonyodev.fetch2.FetchListener
import net.onefivefour.android.bitpot.network.filedownload.FileDownloader
import java.io.File

/**
 * This singleton provides some convenience methods for the data layer.
 */
object BitpotNetwork {

    private lateinit var fileDownloader: FileDownloader

    fun init(context: Context) {
        fileDownloader = FileDownloader(context)
    }

    fun downloadFile(
        downloadId: String,
        downloadUrl: String,
        destinationFile: File,
        downloadListener: FetchListener
    ) {
        fileDownloader.download(downloadId, downloadUrl, destinationFile, downloadListener)
    }
    fun removeDownloadListener(listener: FetchListener) = fileDownloader.removeDownloadListener(listener)
}
