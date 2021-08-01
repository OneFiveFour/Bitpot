package net.onefivefour.android.bitpot.data.database.pagingkeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.paging.PipelinePagingKey

/**
 * This DAO fills and deletes the paging keys for pipelines of a repository.
 */
@Dao
interface PipelinePagingKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(pagingKey: PipelinePagingKey)

    @Query("SELECT nextPageKey FROM pipelinePagingKeys WHERE pipelineIdentifier = :pipelineIdentifier")
    suspend fun getPagingKey(pipelineIdentifier: String): Int?

    @Query("DELETE FROM pipelinePagingKeys WHERE pipelineIdentifier = :pipelineIdentifier")
    suspend fun deletePagingKey(pipelineIdentifier: String)
    
}