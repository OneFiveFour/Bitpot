package net.onefivefour.android.bitpot.data.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.PullRequest

/**
 * An interface describing all possible database operations for the [PullRequest] table
 */
@Dao
interface PullRequestsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pullRequests : List<PullRequest>)

    @Query("SELECT * FROM pullRequests WHERE repoUuid = :repoUuid ORDER BY lastUpdated DESC")
    fun get(repoUuid: String): PagingSource<Int, PullRequest>

    @Query("DELETE FROM pullRequests WHERE repoUuid = :repoUuid")
    suspend fun delete(repoUuid: String)

    @Query("SELECT * FROM pullRequests WHERE repoUuid = :repoUuid")
    fun getAll(repoUuid: String): List<PullRequest>
    
    @Query("SELECT * FROM pullRequests WHERE id = :pullRequestId")
    fun getById(pullRequestId: Int?) : LiveData<PullRequest>
}
