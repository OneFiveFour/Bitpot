package net.onefivefour.android.bitpot.data.model.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A database entity to store paging keys for the list of Pipelines in the app.
 * These paging keys are used in the RemoteMediators to make correct API calls
 * for the next batch of items.
 */
@Entity(tableName = "pipelinePagingKeys")
data class PipelinePagingKey(
    @PrimaryKey
    val pipelineIdentifier: String,
    val nextPageKey: Int
)