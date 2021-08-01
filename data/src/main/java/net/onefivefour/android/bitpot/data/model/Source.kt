package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * The data class for a database entry representing
 * either a directory or a file in the source code of
 * a repository.
  */
@Entity(
    tableName = "sources",
    indices = [Index(value = ["id"], unique = true)]
)
data class Source(
    @PrimaryKey
    val id: String,

    var repoUuid: String,
    var refName: String,
    val path: String,
    val name: String,
    val type: SourceType,
    val size: Long
)

