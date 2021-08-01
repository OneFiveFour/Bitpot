package net.onefivefour.android.bitpot.data.model

import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * This class stores the colors for a specific repository.
 * These colors are for example used to crate the gradient background
 * in RepositoryActivity. A text color is also saved. This text color
 * is promised to provide good contrast to the background gradient.
 */
@Entity(
    tableName = "repositoryColors",
    indices = [Index(value = ["uuid"], unique = true)]
)
data class RepositoryColors(
    @PrimaryKey
    val uuid: String,

    @ColorInt val gradientFromColor: Int,
    @ColorInt val gradientToColor: Int,
    @ColorInt val textColor: Int
)