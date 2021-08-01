package net.onefivefour.android.bitpot.data.repositories

import androidx.lifecycle.LiveData
import net.onefivefour.android.bitpot.data.common.NetworkDataConversion
import net.onefivefour.android.bitpot.data.model.FileDiff
import net.onefivefour.android.bitpot.data.model.Resource
import net.onefivefour.android.bitpot.data.model.converter.DiffConverter
import net.onefivefour.android.bitpot.network.retrofit.BitbucketService

/**
 * This repository provides access to all data that is git diff related.
 */
object DiffRepository {

    /**
     * Returns LiveData containing a list of [FileDiff] for the given [pullRequestId]
     */
    fun getDiff(pullRequestId: Int) : LiveData<Resource<List<FileDiff>>> {
        return object : NetworkDataConversion<String, List<FileDiff>>() {
            override fun getNetworkCall() = BitbucketService.get(getRawStringResponse = true).getDiff(pullRequestId)
            override fun getConverter() = DiffConverter()
        }.get()
    }


}