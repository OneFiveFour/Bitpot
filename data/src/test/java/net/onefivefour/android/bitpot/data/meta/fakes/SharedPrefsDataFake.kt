package net.onefivefour.android.bitpot.data.meta.fakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.onefivefour.android.bitpot.data.ISharedPrefsData

class SharedPrefsDataFake : ISharedPrefsData {

    override fun getSelectedWorkspaceUuidLiveData(): LiveData<String?> {
        return MutableLiveData(selectedWorkspaceUuid)
    }

    override fun getAccountIdLiveData(): LiveData<String> {
        return MutableLiveData(accountId)
    }

    override var selectedWorkspaceUuid: String? = null

    override var firebaseToken = "firebaseToken"

    override var accountId = "accountId"

}