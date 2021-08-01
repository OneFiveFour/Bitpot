package net.onefivefour.android.bitpot.data.repositories

import androidx.lifecycle.LiveData
import net.onefivefour.android.bitpot.data.ISharedPrefsData
import net.onefivefour.android.bitpot.data.common.NetworkBoundResource
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.network.apifields.UserApiFields
import net.onefivefour.android.bitpot.network.common.ApiResponse
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import net.onefivefour.android.bitpot.network.model.user.User as NetworkUser

/**
 * This repository is used to fetch the accountId of the current User as soon as
 * a login was successful. The accountId is stored in [ISharedPrefsData]
 */
class UserRepository(private val sharedPrefsData: ISharedPrefsData) {

    fun getAccountId(): LiveData<Resource<String>> {

        return object : NetworkBoundResource<String, NetworkUser>() {

            override fun saveCallResult(item: NetworkUser) { sharedPrefsData.accountId = item.accountId }

            override fun shouldFetch(data: String?) = true

            override fun loadFromDb(): LiveData<String> = sharedPrefsData.getAccountIdLiveData()

            override fun createCall(): LiveData<ApiResponse<NetworkUser>> = BitbucketService.get(UserApiFields()).getUser()

        }.asLiveData()
    }

}
