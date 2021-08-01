package net.onefivefour.android.bitpot.data

import androidx.lifecycle.LiveData

/**
 * An interface for shared preferences within the data module.
 * This interface is implemented by production and testing code.
 */
interface ISharedPrefsData {
    fun getSelectedWorkspaceUuidLiveData(): LiveData<String?>

    fun getAccountIdLiveData(): LiveData<String>

    var selectedWorkspaceUuid: String?

    var firebaseToken: String

    var accountId: String
}
