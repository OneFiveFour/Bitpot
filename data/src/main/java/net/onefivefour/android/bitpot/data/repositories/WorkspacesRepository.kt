package net.onefivefour.android.bitpot.data.repositories

import net.onefivefour.android.bitpot.data.common.NetworkDataConversion
import net.onefivefour.android.bitpot.data.database.WorkspacesDao
import net.onefivefour.android.bitpot.data.model.Workspace
import net.onefivefour.android.bitpot.data.model.converter.WorkspaceConverter
import net.onefivefour.android.bitpot.network.apifields.WorkspacesApiFields
import net.onefivefour.android.bitpot.network.model.workspace.Workspaces
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit

/**
 * The 'Repository' classes should always be the only api contact for the ui layer.
 * The ViewModels can get all their data from here and should not call any other classes directly.
 *
 * This repository is fetching all workspaces of the user and stores them in the database
 */
class WorkspacesRepository(private val workspacesDao: WorkspacesDao) : KoinComponent {

    fun getWorkspaces() = object : NetworkDataConversion<Workspaces, List<Workspace>>() {
        override fun getNetworkCall() = BitbucketService.get(WorkspacesApiFields()).getWorkspaces()
        override fun getConverter() = WorkspaceConverter()
        override fun shouldFetch(appData: List<Workspace>): Boolean {
            val oldestUpdate = appData.minOf { it.createdOn }
            return oldestUpdate.isBefore(Instant.now().minus(7, ChronoUnit.DAYS))
        }

        override fun loadCachedData() = workspacesDao.get()
        override fun cacheData(appData: List<Workspace>) = workspacesDao.insert(appData)
    }.get()

}