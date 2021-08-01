package net.onefivefour.android.bitpot.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class DiffRepositoryTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Force tests to be executed synchronously

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            startKoin { modules(dataModuleTesting) }
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            stopKoin()
        }
    }

    private val sut = DiffRepository

    @Test
    fun getDiff_returnsLiveData() {
        sut.getDiff(1).test().assertHasValue()
    }

}