package net.onefivefour.android.bitpot.data.database.pagingkeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.paging.CommentPagingKey

/**
 * This DAO fills and deletes the paging keys for Comments of a pull request.
 */
@Dao
interface CommentPagingKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(pagingKey: CommentPagingKey)

    @Query("SELECT nextPageKey FROM commentPagingKeys WHERE commentIdentifier = :commentIdentifier")
    suspend fun getPagingKey(commentIdentifier: Int): Int?

    @Query("DELETE FROM commentPagingKeys WHERE commentIdentifier = :commentIdentifier")
    suspend fun deletePagingKey(commentIdentifier: Int)
    
}