package net.onefivefour.android.bitpot.data.meta

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import net.onefivefour.android.bitpot.data.common.DispatcherProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.KoinComponent
import org.koin.core.inject

@ExperimentalCoroutinesApi
class CoroutineTestRule : TestWatcher(), KoinComponent {

    private val testDispatcherProvider : DispatcherProvider by inject()

    val testDispatcher = testDispatcherProvider.default() as TestCoroutineDispatcher

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcherProvider.default())
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        (testDispatcherProvider.default() as TestCoroutineDispatcher).cleanupTestCoroutines()
    }
}