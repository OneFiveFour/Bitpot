package net.onefivefour.android.bitpot.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.data.common.NetworkDataConversion
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.data.model.converter.StringConverter
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService
import org.koin.core.KoinComponent


/**
 * This repository provides us with all data related to source browsing of a Bitbucket repository.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class SourcesRepository : KoinComponent {

    /**
     * @return the requested File content wrapped in a [Resource] to get network states.
     * Served as observable LiveData instance.
     *
     * @param refName the hash value of the commit that this file is part of
     * @param filePath the path to the file without the file name itself
     * @param fileName the name of the file
     */
    fun getFile(refName: String, filePath: String, fileName: String): LiveData<Resource<String>> {
        return object : NetworkDataConversion<String, String>() {
            override fun getNetworkCall() = BitbucketService.get(getRawStringResponse = true).getFile(refName, filePath, fileName)
            override fun getConverter() = StringConverter()
        }.get()
    }
}