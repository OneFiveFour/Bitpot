package net.onefivefour.android.bitpot.data.model.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A database entity to store paging keys for the list of PR Comments in the app.
 * These paging keys are used in the RemoteMediators to make correct API calls
 * for the next batch of items.
 */
@Entity(tableName = "commentPagingKeys")
data class CommentPagingKey(
    @PrimaryKey
    val commentIdentifier: Int,
    val nextPageKey: Int
)