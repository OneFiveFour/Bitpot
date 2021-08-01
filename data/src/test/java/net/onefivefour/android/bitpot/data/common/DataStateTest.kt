package net.onefivefour.android.bitpot.data.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import net.onefivefour.android.bitpot.network.model.api.NetworkState
import org.junit.Rule
import org.junit.Test

internal class DataStateTest {

    companion object {
        const val ERROR_MESSAGE = "test error message"
    }

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Force tests to be executed synchronously

    @Test
    fun from_networkStateLoaded_returnsDataStateLoaded() {
        DataState.from(NetworkState.LOADED).test()
            .assertHasValue()
            .assertValue(DataState.LOADED)
    }

    @Test
    fun from_networkStateLoading_returnsDataStateLoading() {
        DataState.from(NetworkState.LOADING).test()
            .assertHasValue()
            .assertValue(DataState.LOADING)
    }

    @Test
    fun from_networkStateError_returnsDataStateError() {
        DataState.from(NetworkState.error(ERROR_MESSAGE)).test()
            .assertHasValue()
            .assertValue { it.msg == ERROR_MESSAGE }
            .assertValue { it.status == Status.FAILED }
    }

}