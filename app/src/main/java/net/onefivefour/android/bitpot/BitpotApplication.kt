package net.onefivefour.android.bitpot

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.paging.ExperimentalPagingApi
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.Gson
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.di.dataModule
import net.onefivefour.android.bitpot.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * The Application class of this app.
 * Used to initialize global stuff like libraries.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class BitpotApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize dependency injection
        startKoin {
            // androidLogger(Level.DEBUG)
            androidContext(this@BitpotApplication)
            modules(listOf(dataModule, appModule))
        }

        // Initialize logging
        @Suppress("ConstantConditionIf")
        when {
            BuildConfig.DEBUG -> Timber.plant(DebugTree())
            else -> Timber.plant(CrashReportingTree())
        }

        // Initialize shared preferences
        Kotpref.init(this)
        Kotpref.gson = Gson()

        // Initialize date time library
        AndroidThreeTen.init(this)

        // Set the correct dark mode
        AppCompatDelegate.setDefaultNightMode(SharedPrefsApp.darkMode.toInt())

        // Initialize data module settings
        BitpotData.init(applicationContext)
        BitpotData.setCacheDir(cacheDir)
    }
}