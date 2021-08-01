package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.extensions.toMD5
import net.onefivefour.android.bitpot.data.model.SourceType
import net.onefivefour.android.bitpot.data.model.Source as AppSource
import net.onefivefour.android.bitpot.network.model.sources.Source as NetworkSource

/**
 * Converts a [NetworkSource] into a app domain [net.onefivefour.android.bitpot.data.model.Source].
 */
class SourceConverter : NetworkDataConverter<NetworkSource, AppSource> {

    override fun toAppModel(item: NetworkSource): AppSource {

        val type = when (item.type) {
            "commit_directory" -> SourceType.DIRECTORY
            "commit_file" -> SourceType.FILE
            else -> SourceType.UNKNOWN
        }

        val id = (item.commit.hash + item.path + item.size).toMD5()
        val name = item.path.substringAfterLast("/")
        val path = item.path.removeSuffix(name).removeSuffix("/")

        return AppSource(
            id,
            "", // the repository uuid is set after conversion in [SourcesRemoteMediator]
            "",  // the refName is set after conversion in [SourcesRemoteMediator]
            path,
            name,
            type,
            item.size
        )
    }
}
