package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Data class for the lookup table which [Ref]
 * the user selected for what [Repository]
 */
@Entity(tableName = "refSelections",
    indices = [Index(value = ["repoUuid"], unique = true)])
data class RefSelection(

    @PrimaryKey
    val repoUuid: String,
    val ref: Ref
)