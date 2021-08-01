package net.onefivefour.android.bitpot.data.model

import androidx.annotation.StringRes
import org.threeten.bp.Instant

/**
 * Model class for an item that is listed in the LegalFragment.
 */
data class LegalItem(
    @StringRes val titleRes: Int,
    val lastUpdated: Instant,
    val fileName: String
)
