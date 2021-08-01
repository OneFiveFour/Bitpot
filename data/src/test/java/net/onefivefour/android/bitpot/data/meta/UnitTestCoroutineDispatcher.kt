package net.onefivefour.android.bitpot.data.meta

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import net.onefivefour.android.bitpot.data.common.DispatcherProvider

@ExperimentalCoroutinesApi
class UnitTestCoroutineDispatcher : DispatcherProvider {

    private val testDispatcher = TestCoroutineDispatcher()

    override fun default(): CoroutineDispatcher = testDispatcher
    override fun io(): CoroutineDispatcher = testDispatcher
    override fun main(): CoroutineDispatcher = testDispatcher
    override fun unconfined(): CoroutineDispatcher = testDispatcher
}