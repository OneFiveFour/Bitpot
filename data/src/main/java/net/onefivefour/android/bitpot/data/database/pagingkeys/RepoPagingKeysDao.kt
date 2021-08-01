package net.onefivefour.android.bitpot.data.database.pagingkeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.paging.RepoPagingKey

/**
 * This DAO fills and deletes the paging keys for repositories of all workspaces.
 */
@Dao
interface RepoPagingKeysDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pagingKey: RepoPagingKey)

    @Query("SELECT nextPageKey FROM repoPagingKeys WHERE repoIdentifier = :repoIdentifier")
    suspend fun getPagingKey(repoIdentifier: String): Int?

    @Query("DELETE FROM repoPagingKeys WHERE repoIdentifier = :repoIdentifier")
    suspend fun deletePagingKey(repoIdentifier: String)
    
    
}