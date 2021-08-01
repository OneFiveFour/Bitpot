package net.onefivefour.android.bitpot.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.data.ISharedPrefsData
import net.onefivefour.android.bitpot.data.SharedPrefsData
import net.onefivefour.android.bitpot.data.common.DefaultDispatcherProvider
import net.onefivefour.android.bitpot.data.common.DispatcherProvider
import net.onefivefour.android.bitpot.data.database.BitpotDatabase
import net.onefivefour.android.bitpot.data.model.converter.RepositoryConverter
import net.onefivefour.android.bitpot.data.repositories.*
import net.onefivefour.android.bitpot.network.ISharedPrefsNetwork
import net.onefivefour.android.bitpot.network.SharedPrefsNetwork
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Dependency injection module for all classes and ViewModels defined in the data module.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@Suppress("USELESS_CAST")
val dataModule = module {

    single {
        Room
            .databaseBuilder(androidApplication(), BitpotDatabase::class.java, "bitpot.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<BitpotDatabase>().comments() }
    single { get<BitpotDatabase>().downloads() }
    single { get<BitpotDatabase>().pipelines() }
    single { get<BitpotDatabase>().pullRequests() }
    single { get<BitpotDatabase>().refs() }
    single { get<BitpotDatabase>().refSelection() }
    single { get<BitpotDatabase>().repositories() }
    single { get<BitpotDatabase>().repositoryColors() }
    single { get<BitpotDatabase>().sourcePagingKeys() }
    single { get<BitpotDatabase>().pullRequestPagingKeys() }
    single { get<BitpotDatabase>().pipelinesPagingKeys() }
    single { get<BitpotDatabase>().commentsPagingKeys() }
    single { get<BitpotDatabase>().refPagingKeys() }
    single { get<BitpotDatabase>().downloadPagingKeys() }
    single { get<BitpotDatabase>().repoPagingKeys() }
    single { get<BitpotDatabase>().sources() }
    single { get<BitpotDatabase>().webHooks() }
    single { get<BitpotDatabase>().workspaces() }

    single { CommentsRepository(get()) }
    single { RepoColorsRepository(get()) }
    single { RefsRepository(get()) }
    single { RepoRepository(get()) }
    single { SourcesRepository() }
    single { PullRequestRepository(get()) }
    single { UserRepository(get()) }
    single { WebHooksRepository(get()) }
    single { WorkspacesRepository(get()) }
    single { RepositoryConverter() }

    single { SharedPrefsNetwork(get()) as ISharedPrefsNetwork }
    single { SharedPrefsData(get()) as ISharedPrefsData }

    single { DefaultDispatcherProvider() as DispatcherProvider }
}