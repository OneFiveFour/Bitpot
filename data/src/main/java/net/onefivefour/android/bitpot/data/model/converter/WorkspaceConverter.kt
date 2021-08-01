package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.network.model.workspace.Workspaces
import org.threeten.bp.Instant
import net.onefivefour.android.bitpot.data.model.Workspace as AppWorkspace
import net.onefivefour.android.bitpot.network.model.workspace.Workspace as NetworkWorkspace

/**
 * Converts a [NetworkWorkspace] into a [AppWorkspace].
 */
class WorkspaceConverter : NetworkDataConverter<Workspaces, List<AppWorkspace>> {
    override fun toAppModel(item: Workspaces): List<AppWorkspace> {
        return item.values.map { workspace ->
            
            val uuid = workspace.uuid
            val displayName = workspace.displayName
            val avatarUrl = workspace.links.avatar.href
            
            val createdOn = Instant.now()

            AppWorkspace(
                uuid,
                displayName,
                avatarUrl,
                createdOn
            )
        }
    }
}
