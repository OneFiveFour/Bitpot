package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.Instant


/**
 * The data class representing a Repository.
 * This is also the description of the database table for repositories.
 */
@Entity(
    tableName = "repositories",
    indices = [Index(value = ["uuid"], unique = true)]
)
data class Repository(

    @PrimaryKey
    val uuid: String,
    val name: String,
    val workspaceUuid: String,
    val avatar: RepositoryAvatar,
    val lastUpdated: Instant,
    val isPrivate: Boolean,
    val lastPipelineState: PipelineState,
    val mainBranch: String
)