package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.Instant

/**
 * The data class describing a Pipeline together with it current building state and result.
 */
@Entity(
    tableName = "pipelines",
    indices = [Index(value = ["uuid"], unique = true)]
)
data class Pipeline(

    @PrimaryKey
    val uuid: String,
    val repoUuid: String,
    val buildNumber: Int,
    val target: PipelineTarget,
    val state: PipelineState,
    val createdOn: Instant
)

