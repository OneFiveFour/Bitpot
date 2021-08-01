package net.onefivefour.android.bitpot.data.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import org.junit.Rule
import org.junit.Test

internal class AbsentLiveDataTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Force tests to be executed synchronously

    @Test
    fun create_returnsEmptyLiveData() {
        AbsentLiveData.create<Unit>().test()
            .assertValue(null)
    }

}