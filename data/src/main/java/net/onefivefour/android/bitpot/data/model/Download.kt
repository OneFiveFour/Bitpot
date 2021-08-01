package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.Instant

/**
 * The data class representing a Download.
 * This is also the description of the database table for downloads.
 */
@Entity(
    tableName = "downloads",
    indices = [Index(value = ["id"], unique = true)]
)
data class Download(
    @PrimaryKey
    val id: String,

    var repoUuid: String = "",
    val name: String,
    val createdOn: Instant,
    val fileSize: Long,
    val downloadUrl: String,
    var downloadProgress: Float
)