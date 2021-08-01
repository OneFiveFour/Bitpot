package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * The data class for a database entry representing
 * either a branch or a tag of a repository.
 */
@Entity(
    tableName = "refs",
    indices = [Index(value = ["id"], unique = true)]
)
data class Ref(
    @PrimaryKey
    val id: String,

    val repoUuid: String,
    val name: String,
    val hash: String,
    val type: RefType
)