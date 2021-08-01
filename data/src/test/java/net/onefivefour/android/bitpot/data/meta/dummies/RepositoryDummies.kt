package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.data.extensions.toInstant
import net.onefivefour.android.bitpot.data.model.Repository as AppRepository
import net.onefivefour.android.bitpot.network.model.pipelines.Repository as PipelinesRepository
import net.onefivefour.android.bitpot.network.model.pullrequests.Repository as PullRequestsRepository
import net.onefivefour.android.bitpot.network.model.refs.Repository as RefRepository
import net.onefivefour.android.bitpot.network.model.repositories.Repository as NetworkRepository

object RepositoryDummies {

    fun getRepository(): NetworkRepository {

        val fullName = "Repository.fullName"
        val isPrivate = false
        val name = "Repository.name"
        val workspace = WorkspaceDummies.getNetworkRepositoryWorkspace()
        val updatedOn = StringDummies.getDateTimeString()
        val uuid = "Repository.uuid"
        val links = LinksDummies.getRepositoryLinks()
        val mainBranch = BranchDummies.getMainBranch()

        return NetworkRepository(
            fullName,
            isPrivate,
            links,
            name,
            workspace,
            updatedOn,
            uuid,
            mainBranch
        )
    }

    fun getSimpleAppRepository(): AppRepository {

        val uuid = "Repository.uuid"
        val name = "Repository.name"
        val workspaceUuid = "Repository.workspaceUuid"
        val avatar = RepositoryAvatarDummies.getSimpleRepositoryLanguageAvatar()
        val lastUpdated = StringDummies.getDateTimeString().toInstant()
        val isPrivate = false
        val lastPipelineState = PipelineStateDummies.getSimpleAppPipelineState()
        val mainBranch = "master"

        return AppRepository(
            uuid,
            name,
            workspaceUuid,
            avatar,
            lastUpdated,
            isPrivate,
            lastPipelineState,
            mainBranch
        )
    }

    fun getPullRequestsRepository(): PullRequestsRepository {
        val uuid = "Repository.uuid"
        return PullRequestsRepository(uuid)
    }

    fun getPipelinesRepository(): PipelinesRepository {
        val uuid = "Repository.uuid"
        return PipelinesRepository(uuid)
    }

    fun getRefRepository(): RefRepository {
        val uuid = "Repository.uuid"
        return RefRepository(uuid)
    }

}