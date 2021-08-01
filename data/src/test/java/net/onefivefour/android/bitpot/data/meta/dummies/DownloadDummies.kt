package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.data.extensions.toInstant
import net.onefivefour.android.bitpot.data.model.Download as AppDownload
import net.onefivefour.android.bitpot.network.model.downloads.Download as NetworkDownload

object DownloadDummies {

    fun getAppDownload(): AppDownload {

        val id = "Download.uuid"
        val repoUuid = "Repository.uuid"
        val name = "Download.name"
        val createdOn = StringDummies.getDateTimeString().toInstant()
        val fileSize = 123L
        val downloadUrl = StringDummies.getDownloadUrl()
        val downloadProgress = 42f

        return AppDownload(
            id,
            repoUuid,
            name,
            createdOn,
            fileSize,
            downloadUrl,
            downloadProgress
        )
    }

    fun getNetworkDownload() : NetworkDownload {

        val createdOn = StringDummies.getDateTimeString()
        val links = LinksDummies.getDownloadLinks()
        val name = "Download.name"
        val size = 123L

        return NetworkDownload(
            createdOn,
            links,
            name,
            size
        )
    }


}
