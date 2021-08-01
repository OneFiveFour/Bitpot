package net.onefivefour.android.bitpot.di

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.paging.ExperimentalPagingApi
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.analytics.EventTracker
import net.onefivefour.android.bitpot.data.common.RuntimeTypeAdapterFactory
import net.onefivefour.android.bitpot.data.model.notifications.*
import net.onefivefour.android.bitpot.screens.auth.AuthViewModel
import net.onefivefour.android.bitpot.screens.auth.ConsentViewModel
import net.onefivefour.android.bitpot.screens.downloads.DownloadsViewModel
import net.onefivefour.android.bitpot.screens.file.FileViewModel
import net.onefivefour.android.bitpot.screens.legal.LegalViewModel
import net.onefivefour.android.bitpot.screens.main.UserViewModel
import net.onefivefour.android.bitpot.screens.main.WorkspacesViewModel
import net.onefivefour.android.bitpot.screens.pipelines.PipelinesViewModel
import net.onefivefour.android.bitpot.screens.pullrequest.CommentViewModel
import net.onefivefour.android.bitpot.screens.pullrequest.DiffViewModel
import net.onefivefour.android.bitpot.screens.pullrequest.PullRequestViewModel
import net.onefivefour.android.bitpot.screens.pullrequests.PullRequestsViewModel
import net.onefivefour.android.bitpot.screens.repositories.RepositoriesViewModel
import net.onefivefour.android.bitpot.screens.repository.RepositoryViewModel
import net.onefivefour.android.bitpot.screens.repository.WebHookViewModel
import net.onefivefour.android.bitpot.screens.sources.RefViewModel
import net.onefivefour.android.bitpot.screens.sources.SourcesViewModel
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Dependency injection module for all classes and ViewModels defined in the app module.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
val appModule = module {

    single {
        val sharedPreferenceKey = androidContext().getString(R.string.preference_file_name)
        androidContext().getSharedPreferences(sharedPreferenceKey, Context.MODE_PRIVATE)
    }

    single { AuthorizationService(androidContext()) }

    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    single { NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build() }

    single {

        val notificationTypeFactory = RuntimeTypeAdapterFactory
            .of(NotificationData::class.java)
            .registerSubtype(PipelineStateUpdated::class.java, "pipeline-state-updated")
            .registerSubtype(PullRequestApproved::class.java, "pull-request-approved")
            .registerSubtype(PullRequestUnapproved::class.java, "pull-request-unapproved")
            .registerSubtype(PullRequestCommentCreated::class.java, "pull-request-comment-created")
            .registerSubtype(PullRequestCreated::class.java, "pull-request-created")

        GsonBuilder()
            .registerTypeAdapterFactory(notificationTypeFactory)
            .create()
    }

    single { WorkManager.getInstance(androidContext()) }

    single { FirebaseAnalytics.getInstance(androidContext()) }

    single { EventTracker(get()) }

    single {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            Resources.getSystem().configuration.locale
        }
    }

    viewModel { RepositoriesViewModel(get()) }
    viewModel { (workspaceUuid: String, repositoryUuid: String) -> RepositoryViewModel(get(), get(), workspaceUuid, repositoryUuid) }
    viewModel { AuthViewModel() }
    viewModel { WorkspacesViewModel(get()) }
    viewModel { PipelinesViewModel(get()) }
    viewModel { PullRequestsViewModel(get()) }
    viewModel { DownloadsViewModel(get()) }
    viewModel { SourcesViewModel(get(), get()) }
    viewModel { RefViewModel(get(), get()) }
    viewModel { FileViewModel(get()) }
    viewModel { PullRequestViewModel(get()) }
    viewModel { DiffViewModel() }
    viewModel { CommentViewModel(get()) }
    viewModel { (workspaceUuid: String, repositoryUuid: String) -> WebHookViewModel(get(), get(), workspaceUuid, repositoryUuid) }
    viewModel { UserViewModel(get()) }
    viewModel { LegalViewModel(get()) }
    viewModel { ConsentViewModel(get()) }
}