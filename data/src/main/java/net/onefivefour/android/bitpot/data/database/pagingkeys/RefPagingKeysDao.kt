package net.onefivefour.android.bitpot.data.database.pagingkeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.paging.RefPagingKey

/**
 * This DAO fills and deletes the paging keys for reference (branch/tags) of all repositories.
 */
@Dao
interface RefPagingKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pagingKey: RefPagingKey)

    @Query("SELECT nextPageKey FROM refPagingKeys WHERE refIdentifier = :refIdentifier")
    suspend fun getPagingKey(refIdentifier: String): String?

    @Query("DELETE FROM refPagingKeys WHERE refIdentifier = :refIdentifier")
    suspend fun deletePagingKey(refIdentifier: String)
    
}