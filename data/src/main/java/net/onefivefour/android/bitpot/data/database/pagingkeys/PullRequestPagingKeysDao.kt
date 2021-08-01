package net.onefivefour.android.bitpot.data.database.pagingkeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.paging.PullRequestPagingKey

/**
 * This DAO fills and deletes the paging keys for pull requests of a repository.
 */
@Dao
interface PullRequestPagingKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(pagingKey: PullRequestPagingKey)

    @Query("SELECT nextPageKey FROM pullRequestPagingKeys WHERE pullRequestIdentifier = :pullRequestIdentifier")
    suspend fun getPagingKey(pullRequestIdentifier: String): Int?

    @Query("DELETE FROM pullRequestPagingKeys WHERE pullRequestIdentifier = :pullRequestIdentifier")
    suspend fun deletePagingKey(pullRequestIdentifier: String)
    
}