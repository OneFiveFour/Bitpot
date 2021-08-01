package net.onefivefour.android.bitpot.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.gsonNullablePref
import com.chibatching.kotpref.livedata.asLiveData

/**
 * KotprefModel for all SharedPreferences in the app module.
 */
class SharedPrefsData(context: Context) : KotprefModel(context), ISharedPrefsData {

    override val kotprefName: String = "${context.packageName}_preferences_data"

    override var selectedWorkspaceUuid by gsonNullablePref<String>()

    /**
     * The firebase token we receive when the device is registered on the firebase server
     */
    override var firebaseToken by stringPref("")

    override var accountId by stringPref("")

    override fun getSelectedWorkspaceUuidLiveData(): LiveData<String?> {
        return asLiveData(this::selectedWorkspaceUuid)
    }

    override fun getAccountIdLiveData(): LiveData<String> {
        return asLiveData(this::accountId)
    }
}