package net.onefivefour.android.bitpot.data.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.Repository

/**
 * An interface describing all possible database operations for the [Repository] table
 */
@Dao
interface RepositoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repositories : List<Repository>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repository : Repository)

    @Query("SELECT * FROM repositories WHERE workspaceUuid = :workspaceUuid ORDER BY lastUpdated DESC")
    fun getByWorkspaceUuid(workspaceUuid: String): PagingSource<Int, Repository>

    @Query("SELECT * FROM repositories WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): LiveData<Repository>

    @Query("DELETE FROM repositories WHERE workspaceUuid = :workspaceUuid")
    suspend fun delete(workspaceUuid: String)

    @Query("SELECT * FROM repositories")
    fun getAll(): List<Repository>
}
