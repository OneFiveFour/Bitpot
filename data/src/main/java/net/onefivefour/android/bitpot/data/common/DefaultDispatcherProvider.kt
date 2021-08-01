package net.onefivefour.android.bitpot.data.common

/**
 * This is the default implementation of [DispatcherProvider]
 * Always use this provider in production code to allow unit tests
 * of this particular code.
 *
 * For more details see: https://craigrussell.io/2019/11/unit-testing-coroutine-suspend-functions-using-testcoroutinedispatcher/
 */
class DefaultDispatcherProvider : DispatcherProvider