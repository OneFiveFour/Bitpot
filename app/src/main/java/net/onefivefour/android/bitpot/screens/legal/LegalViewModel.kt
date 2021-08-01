package net.onefivefour.android.bitpot.screens.legal

import androidx.lifecycle.ViewModel
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.LegalItem
import org.threeten.bp.Instant
import java.util.*

/**
 * A simple View Model for the [net.onefivefour.android.bitpot.screens.markdown.MarkdownFragment].
 * Used to fill it with markdown content.
 */
class LegalViewModel(deviceLocale: Locale) : ViewModel() {

    val imprint = LegalItem(
        R.string.imprint,
        Instant.parse("2020-05-18T12:00:00.000Z"),
        "imprint.md"
    )

    val privacy = if (deviceLocale == Locale.GERMANY) {
        LegalItem(
            R.string.privacy_policy,
            Instant.parse("2021-08-01T12:00:00.000Z"),
            "privacy-de.md"
        )
    } else {
        LegalItem(
            R.string.privacy_policy,
            Instant.parse("2021-08-01T12:00:00.000Z"),
            "privacy-en.md"
        )
    }


    private val licenses = LegalItem(
        R.string.third_party_licenses,
        Instant.parse("2020-05-18T20:08:00.000Z"),
        "licenses.md"
    )

    val legalItems = listOf(
        imprint,
        privacy,
        licenses
    )

}
