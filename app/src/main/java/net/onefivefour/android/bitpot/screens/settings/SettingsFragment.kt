package net.onefivefour.android.bitpot.screens.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.SharedPrefsApp
import net.onefivefour.android.bitpot.screens.auth.ConsentViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * This Fragment is a PreferenceFragment to compose the settings screen
 * based on the given settings.xml
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val sharedPrefs: SharedPreferences by inject()

    private val consentViewModel: ConsentViewModel by sharedViewModel()

    // This listener must be held in a class field to prevent it from being garbage collected.
    // see https://stackoverflow.com/a/3104265/990129
    private val listener = BitpotPreferenceChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.preference_file_name)
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(SharedPrefsApp.darkMode.toInt())
        (requireActivity() as AppCompatActivity).delegate.applyDayNight()
    }

    /**
     * This listener is called whenever a preference is changed.
     */
    inner class BitpotPreferenceChangeListener : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (sharedPreferences == null || key.isNullOrEmpty()) return
            if (!isAdded) return
            when (key) {
                getString(R.string.preference_dark_mode_key) -> setDarkMode()
                getString(R.string.preference_privacy_analytics_key) -> consentViewModel.setAnalyticsConsent(SharedPrefsApp.analyticsConsentGiven)
                getString(R.string.preference_privacy_personalized_ads_key) -> consentViewModel.setPersonalizedAdsConsent(SharedPrefsApp.personalizedAdsConsentGiven)
            }
        }
    }


}