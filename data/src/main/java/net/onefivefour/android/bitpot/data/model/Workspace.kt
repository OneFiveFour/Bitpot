package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.Instant

/**
 * The data class representing a User
 */
@Entity(
    tableName = "workspaces",
    indices = [Index(value = ["uuid"], unique = true)]
)
data class Workspace(
    @PrimaryKey
    val uuid: String,

    val displayName: String,
    val avatarUrl: String,
    val createdOn: Instant
)