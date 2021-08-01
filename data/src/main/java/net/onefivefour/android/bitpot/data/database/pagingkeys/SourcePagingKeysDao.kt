package net.onefivefour.android.bitpot.data.database.pagingkeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.paging.SourcePagingKey

/**
 * This DAO fills and deletes the paging keys for sources of all repositories.
 */
@Dao
interface SourcePagingKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(pagingKey: SourcePagingKey)

    @Query("SELECT nextPageKey FROM sourcePagingKeys WHERE sourceIdentifier = :sourceIdentifier")
    suspend fun getPagingKey(sourceIdentifier: String): String?

    @Query("DELETE FROM sourcePagingKeys WHERE sourceIdentifier = :sourceIdentifier")
    suspend fun deletePagingKey(sourceIdentifier: String)
    
}