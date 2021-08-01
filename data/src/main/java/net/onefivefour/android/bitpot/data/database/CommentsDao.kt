package net.onefivefour.android.bitpot.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.Comment

/**
 * An interface describing all possible database operations for the [Comment] table
 */
@Dao
interface CommentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comments: List<Comment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: Comment)

    @Query("DELETE FROM comments WHERE id = :commentId")
    fun delete(commentId: Int)

    @Query("SELECT * FROM comments WHERE pullRequestId = :pullRequestId")
    fun getByPullRequestId(pullRequestId: Int): LiveData<List<Comment>>

    @Query("DELETE FROM comments WHERE pullRequestId = :pullRequestId")
    suspend fun deleteByPullRequestId(pullRequestId: Int)

    @Query("SELECT * FROM comments")
    fun getAll(): List<Comment>
}
