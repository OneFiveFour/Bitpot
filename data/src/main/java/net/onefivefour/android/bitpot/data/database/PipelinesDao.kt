package net.onefivefour.android.bitpot.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.Pipeline

/**
 * An interface describing all possible database operations for the [Pipeline] table
 */
@Dao
interface PipelinesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pipelines : List<Pipeline>)

    @Query("SELECT * FROM pipelines WHERE repoUuid = :repoUuid ORDER BY createdOn DESC")
    fun get(repoUuid: String): PagingSource<Int, Pipeline>

    @Query("DELETE FROM pipelines WHERE repoUuid = :repoUuid")
    suspend fun delete(repoUuid: String)

    @Query("SELECT * FROM pipelines WHERE repoUuid = :repoUuid")
    fun getAll(repoUuid: String): List<Pipeline>
}
