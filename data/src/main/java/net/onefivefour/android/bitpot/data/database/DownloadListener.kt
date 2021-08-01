package net.onefivefour.android.bitpot.data.database

import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import net.onefivefour.android.bitpot.network.BitpotNetwork
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.concurrent.Executors

/**
 * The implementation of a fetch2 [FetchListener].
 * It is used to react on all possible Callbacks for files being downloaded using
 * the fetch2 library.
 */
class DownloadListener : FetchListener, KoinComponent {

    private val downloadsDao: DownloadsDao by inject()

    override fun onAdded(download: Download) {
        Timber.i("DownloadListener onAdded called")
    }

    override fun onCancelled(download: Download) {
        removeListener()
    }

    override fun onCompleted(download: Download) {
        removeListener()
    }

    override fun onDeleted(download: Download) {
        removeListener()
    }

    override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {
        Timber.i("DownloadListener onDownloadBlockUpdated called")
    }

    override fun onError(download: Download, error: Error, throwable: Throwable?) {
        removeListener()
    }

    override fun onPaused(download: Download) {
        Timber.i("DownloadListener onPaused called")
    }

    override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
        Executors.newSingleThreadExecutor().execute {
            updateDownloadProgress(download)
        }
    }

    override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
        Executors.newSingleThreadExecutor().execute {
            updateDownloadProgress(download)
        }
    }

    private fun updateDownloadProgress(download: Download) {
        val downloadId = download.tag ?: return
        val progress = download.progress.toFloat()
        downloadsDao.updateDownloadProgress(downloadId, progress)
    }

    override fun onRemoved(download: Download) {
        removeListener()
    }

    override fun onResumed(download: Download) {
        Timber.i("DownloadListener onResumed called")
    }

    override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
        Timber.i("DownloadListener onStarted called")

    }

    override fun onWaitingNetwork(download: Download) {
        Timber.i("DownloadListener onWaitingNetwork called")
    }

    private fun removeListener() {
        BitpotNetwork.removeDownloadListener(this)
    }


}