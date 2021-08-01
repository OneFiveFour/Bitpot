package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.extensions.toInstant
import net.onefivefour.android.bitpot.data.extensions.toMD5
import net.onefivefour.android.bitpot.data.model.Download as AppDownload
import net.onefivefour.android.bitpot.network.model.downloads.Download as NetworkDownload

/**
 * Converts a [NetworkDownload] into a app domain [net.onefivefour.android.bitpot.data.model.Download].
 */
class DownloadConverter : NetworkDataConverter<NetworkDownload, AppDownload> {

    override fun toAppModel(item: NetworkDownload): AppDownload {

        // create our own database id from some item params
        // This id should only be the same for the same file name.
        // Do not inlcude e.g. "createdOn" in this id, since then already
        // downloaded files would not be recognized/updated anymore.
        val id = item.name.toMD5()

        return AppDownload(
            id,
            "", // the repositoryUuid is set after conversion in [DownloadsRepository]
            item.name,
            item.createdOn.toInstant(),
            item.size,
            item.links.self.href,
            0f
        )
    }
}
