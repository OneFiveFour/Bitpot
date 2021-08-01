package net.onefivefour.android.bitpot.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.Source

/**
 * An interface describing all possible database operations for the [Source] table
 */
@Dao
interface SourcesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sources : List<Source>)

    @Query("SELECT * FROM sources WHERE repoUuid = :repoUuid AND refName = :refName AND path = :path ORDER BY type, path ASC")
    fun get(repoUuid: String, refName: String, path: String): PagingSource<Int, Source>

    @Query("DELETE FROM sources where repoUuid = :repoUuid")
    suspend fun delete(repoUuid: String)

}
