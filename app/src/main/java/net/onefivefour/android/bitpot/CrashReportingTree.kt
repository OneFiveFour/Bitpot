package net.onefivefour.android.bitpot

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber


/**
 * A Timber tree which logs important information for crash reporting.
 **/
class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        // don't report log to Crashlytics if priority is Verbose or Debug
        if (priority == Log.VERBOSE || priority == Log.DEBUG) return

        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log(message)

        if (priority == Log.ERROR) {
            val exception = throwable ?: Exception(message)
            crashlytics.recordException(exception)
        }
    }
}