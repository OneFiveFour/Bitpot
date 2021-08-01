package net.onefivefour.android.bitpot.screens.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chibatching.kotpref.livedata.asLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.UserProperty.ALLOW_AD_PERSONALIZATION_SIGNALS
import net.onefivefour.android.bitpot.SharedPrefsApp
import timber.log.Timber

/**
 * This ViewModel takes care of handling the privacy consent of the user.
 */
class ConsentViewModel(private val firebaseAnalytics: FirebaseAnalytics): ViewModel() {

    fun getCrashlyticsConsent(): LiveData<Boolean> = SharedPrefsApp.asLiveData(SharedPrefsApp::crashlyticsConsentGiven)

    fun grantCrashlyticsConsent() { SharedPrefsApp.crashlyticsConsentGiven = true }

    fun setAnalyticsConsent(isGiven: Boolean) {
        SharedPrefsApp.analyticsConsentGiven = isGiven
        applyConsentSettings()
    }

    fun setPersonalizedAdsConsent(isGiven: Boolean) {
        SharedPrefsApp.personalizedAdsConsentGiven = isGiven
        applyConsentSettings()
    }

    fun isCrashlyticsConsentGiven() = SharedPrefsApp.crashlyticsConsentGiven

    private fun isAnalyticsConsentGiven() = SharedPrefsApp.analyticsConsentGiven
    private fun isPersonalizedAdsConsentGiven() = SharedPrefsApp.personalizedAdsConsentGiven


    private fun applyConsentSettings() {
        Timber.d("+++ apply consent settings. Analytics: ${isAnalyticsConsentGiven()} | Ads: ${isPersonalizedAdsConsentGiven()}")
        firebaseAnalytics.setAnalyticsCollectionEnabled(isAnalyticsConsentGiven())
        firebaseAnalytics.setUserProperty(ALLOW_AD_PERSONALIZATION_SIGNALS, isPersonalizedAdsConsentGiven().toString())
    }

}