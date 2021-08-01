package net.onefivefour.android.bitpot.data.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.data.ISharedPrefsData
import net.onefivefour.android.bitpot.data.common.DispatcherProvider
import net.onefivefour.android.bitpot.data.database.BitpotDatabase
import net.onefivefour.android.bitpot.data.meta.UnitTestCoroutineDispatcher
import net.onefivefour.android.bitpot.data.meta.fakes.SharedPrefsDataFake
import net.onefivefour.android.bitpot.data.meta.fakes.SharedPrefsNetworkFake
import net.onefivefour.android.bitpot.network.ISharedPrefsNetwork
import org.koin.dsl.module

/**
 * Dependency injection module for all test cases of the data module.
 */
@ExperimentalCoroutinesApi
val dataModuleTesting = module {

    single { ApplicationProvider.getApplicationContext() as Context }

    single {
        val database = Room.inMemoryDatabaseBuilder(get(), BitpotDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        spyk(database)
    }

    single { SharedPrefsNetworkFake() as ISharedPrefsNetwork }
    single { SharedPrefsDataFake() as ISharedPrefsData }

    single { UnitTestCoroutineDispatcher() as DispatcherProvider }
}