package net.onefivefour.android.bitpot.data.database.pagingkeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.paging.DownloadPagingKey

/**
 * This DAO fills and deletes the paging keys for downloads of a repository.
 */
@Dao
interface DownloadPagingKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(pagingKey: DownloadPagingKey)

    @Query("SELECT nextPageKey FROM downloadPagingKeys WHERE downloadIdentifier = :downloadIdentifier")
    suspend fun getPagingKey(downloadIdentifier: String): Int?

    @Query("DELETE FROM downloadPagingKeys WHERE downloadIdentifier = :downloadIdentifier")
    suspend fun deletePagingKey(downloadIdentifier: String)
    
}