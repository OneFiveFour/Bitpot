package net.onefivefour.android.bitpot.data.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * This interface enables injection of CoroutineDispatchers.
 * Production code should always use the default implementation of this interface:
 * [DefaultDispatcherProvider]
 *
 * Unit Test may implement their own version of this provider to allow testing of
 * co-routines.
 *
 * For more information see: https://craigrussell.io/2019/11/unit-testing-coroutine-suspend-functions-using-testcoroutinedispatcher/
 */
interface DispatcherProvider {

    fun main(): CoroutineDispatcher = Dispatchers.Main
    fun default(): CoroutineDispatcher = Dispatchers.Default
    fun io(): CoroutineDispatcher = Dispatchers.IO
    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined

}