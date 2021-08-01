package net.onefivefour.android.bitpot.data.common

import android.content.Context
import android.os.Environment
import net.onefivefour.android.bitpot.data.model.Download
import timber.log.Timber
import java.io.File

/**
 * Helper class for File operations.
 * Mostly used for downloaded files.
 */
object FileHelper {

    private lateinit var downloadDirectory: String

    /**
     * @return the file name of the given url
     */
    private fun extractFileName(url: String): String {
        val startIndex = url.lastIndexOf("/") + 1
        return url.substring(startIndex)
    }

    /**
     * @return a File extracted from the given [Download]
     */
    fun getDownloadFile(download: Download): File {
        val downloadUrl = download.downloadUrl
        val fileName = extractFileName(downloadUrl)
        return File(downloadDirectory, fileName)
    }

    /**
     * set the path for all downloads of Bitpot.
     * Must be called before any other method of FileHelper is used.
     */
    fun setDownloadPath(context: Context) {
        downloadDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path ?: "/"
    }

    /**
     * @return the destination File for the given url.
     */
    fun getDownloadDestination(url: String): File {
        val fileName = extractFileName(url)
        return File(downloadDirectory, fileName)
    }

    /**
     * @return true if the given [Download] already exists in storage.
     */
    fun downloadExists(download: Download): Boolean {
        return getDownloadDestination(download.downloadUrl).exists()
    }

    /**
     * deletes the given [Download] from the file system
     */
    fun deleteDownload(download: Download) {
        val file = getDownloadDestination(download.downloadUrl)
        val result = file.delete()
        Timber.d("+++++ File deleted: $result (${file.absolutePath})")
    }
}