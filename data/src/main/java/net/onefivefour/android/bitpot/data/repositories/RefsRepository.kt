package net.onefivefour.android.bitpot.data.repositories

import kotlinx.coroutines.withContext
import net.onefivefour.android.bitpot.data.common.DispatcherProvider
import net.onefivefour.android.bitpot.data.database.RefSelectionsDao
import net.onefivefour.android.bitpot.data.model.Ref
import net.onefivefour.android.bitpot.data.model.RefSelection
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.Executors

/**
 * This Repository provides all data related to [Ref]s
 */
class RefsRepository(private val refSelectionsDao: RefSelectionsDao)  : KoinComponent  {

    private val dispatcherProvider: DispatcherProvider by inject()
    
    /**
     * @return the currently selected reference (i.e. Branch or Tag) of the given repository
     */
    suspend fun getRefSelection(repositoryUuid: String): RefSelection? {
        return withContext(dispatcherProvider.io()) {
            refSelectionsDao.getByRepoUuid(repositoryUuid)
        }
    }

    /**
     * store the currently selected Ref for the given repository
     * in the database.
     */
    fun setRefSelection(ref: Ref, repoUuid: String) {
        Executors.newSingleThreadExecutor().execute {
            val refSelection = RefSelection(repoUuid, ref)
            refSelectionsDao.insert(refSelection)
        }
    }
    
    
}