package net.onefivefour.android.bitpot.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.Download

/**
 * An interface describing all possible database operations for the [Download] table
 */
@Dao
interface DownloadsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(downloads : List<Download>)

    @Query("SELECT * FROM downloads WHERE id = :downloadId")
    suspend fun get(downloadId: String) : Download?

    @Query("SELECT * FROM downloads WHERE repoUuid = :repoUuid ORDER BY createdOn DESC")
    fun getByRepoUuid(repoUuid: String): PagingSource<Int, Download>

    @Query("DELETE FROM downloads WHERE repoUuid = :repoUuid")
    suspend fun delete(repoUuid: String)

    @Query("SELECT * FROM downloads WHERE repoUuid = :repoUuid")
    fun getAll(repoUuid: String): List<Download>

    @Query("UPDATE downloads SET downloadProgress = :progress WHERE id = :downloadId")
    fun updateDownloadProgress(downloadId: String, progress: Float)
}
