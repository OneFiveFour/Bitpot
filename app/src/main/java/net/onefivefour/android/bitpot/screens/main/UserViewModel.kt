package net.onefivefour.android.bitpot.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import net.onefivefour.android.bitpot.data.model.ResourceStatus
import net.onefivefour.android.bitpot.data.repositories.UserRepository

/**
 * A simple ViewModel to fetch the user object from the Bitbucket API.
 * All we need from this user is the account id.
 */
class UserViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getAccountId() : LiveData<String> = Transformations.map(userRepository.getAccountId()) { resource ->
        when (resource.resourceStatus) {
            ResourceStatus.SUCCESS -> resource.data
            ResourceStatus.ERROR -> ""
            ResourceStatus.LOADING -> ""
        }
    }
}
