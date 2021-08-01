package net.onefivefour.android.bitpot.data.meta.dummies

import org.threeten.bp.Instant
import net.onefivefour.android.bitpot.data.model.Workspace as AppWorkspace
import net.onefivefour.android.bitpot.network.model.repositories.Workspace as NetworkRepositoryWorkspace
import net.onefivefour.android.bitpot.network.model.workspace.Workspace as NetworkWorkspace
import net.onefivefour.android.bitpot.network.model.workspace.Workspaces as NetworkWorkspaces

object WorkspaceDummies {


    fun getNetworkRepositoryWorkspace(): NetworkRepositoryWorkspace {
        val uuid = "Workspace.uuid"
        return NetworkRepositoryWorkspace(uuid)
    }

    private fun getNetworkWorkspace(): NetworkWorkspace {

        val links = LinksDummies.getWorkspaceLinks()
        val displayName = StringDummies.getDisplayName()
        val uuid = "Workspace.uuid"

        return NetworkWorkspace(
            links,
            displayName,
            uuid
        )

    }
    
    fun getNetworkWorkspaces() : NetworkWorkspaces {
        
        val page = 1
        val pageLength = 1
        val workspaces = listOf(getNetworkWorkspace())
        val totalItems = workspaces.size
        
        return NetworkWorkspaces(
            page,
            pageLength,
            workspaces,
            totalItems,
            null
        )
    }

    fun getAppWorkspace(): AppWorkspace {

        val uuid = "Workspace.uuid"
        val displayName = "Workspace.displayName"
        val avatarUrl = "Workspace.avatarUrl"
        val createdOn = Instant.now()

        return AppWorkspace(
            uuid,
            displayName,
            avatarUrl,
            createdOn
        )
    }

}