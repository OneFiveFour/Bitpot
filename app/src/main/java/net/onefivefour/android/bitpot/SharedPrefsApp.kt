package net.onefivefour.android.bitpot

import com.chibatching.kotpref.KotprefModel

/**
 * KotprefModel for all SharedPreferences in the app module.
 */
object SharedPrefsApp : KotprefModel() {

    override val kotprefName: String = context.getString(R.string.preference_file_name)

    /**
     * Boolean flag to save the users preference in the file viewer.
     * if true, line wrap was enabled. False otherwise.
     * Default is false.
     */
    var fileViewerLineWrapEnabled by booleanPref(false)

    /**
     * Boolean flag to save the users preference in the file viewer.
     * If true, line numbers are visible. False otherwise.
     * Default is true.
     */
    var fileViewerLineNumbersVisible by booleanPref(true)

    /**
     * Boolean flag to save whether the user realized that the WebView
     * in the FileActivity can be pulled up.
     */
    var userSwipedUpFileViewer by booleanPref(false)


    /**
     * Boolean flag to store that the user clicked the "Agree" button in
     * the AuthActivity to give his/her consent for handling crashlytics data.
     */
    var crashlyticsConsentGiven by booleanPref(false)

    /**
     * Preferences set in the [net.onefivefour.android.bitpot.screens.settings.SettingsFragment]
     * Saved the users preference between light mode, dark mode and "decide by battery/system settings"
     */
    var darkMode by stringPref(context.getString(R.string.preference_dark_mode_default), R.string.preference_dark_mode_key)

    /**
     * Preferences set in the [net.onefivefour.android.bitpot.screens.settings.SettingsFragment]
     * Saved the users consent to track his/her actions using Firebase Analytics.
     */
    var analyticsConsentGiven by booleanPref(false, R.string.preference_privacy_analytics_key)

    /**
     * Preferences set in the [net.onefivefour.android.bitpot.screens.settings.SettingsFragment]
     * Saved the users consent to show him/her personalized ads.
     */
    var personalizedAdsConsentGiven by booleanPref(false, R.string.preference_privacy_personalized_ads_key)

}