package net.onefivefour.android.bitpot.screens.downloads

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.analytics.AnalyticsEvent
import net.onefivefour.android.bitpot.analytics.EventTracker
import net.onefivefour.android.bitpot.analytics.model.ContentType
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.common.SnackBarPresenter
import net.onefivefour.android.bitpot.common.SpaceItemDecoration
import net.onefivefour.android.bitpot.data.common.FileHelper
import net.onefivefour.android.bitpot.data.model.Download
import net.onefivefour.android.bitpot.databinding.FragmentDownloadsBinding
import net.onefivefour.android.bitpot.databinding.isLoading
import net.onefivefour.android.bitpot.databinding.isRefreshing
import net.onefivefour.android.bitpot.databinding.setVisibilityGone
import net.onefivefour.android.bitpot.screens.repository.RepositoryViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

/**
 * This fragment shows a list of all available downloads of a repository.
 * The user can click on each download to start downloading the file.
 * When the download finished, a notification informs the user about it, so he
 * can directly navigate to the download folder.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class DownloadsFragment : Fragment(), ItemClickListener {

    private lateinit var binding: FragmentDownloadsBinding
    
    private val eventTracker: EventTracker by inject()

    private val downloadsViewModel: DownloadsViewModel by viewModel()
    
    private val repositoryViewModel: RepositoryViewModel by sharedViewModel()

    private lateinit var snackBarPresenter: SnackBarPresenter
    
    private val downloadsAdapter = DownloadsAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is SnackBarPresenter -> snackBarPresenter = context
            else -> throw IllegalArgumentException("Given Context must implement SnackBarPresenter interface!")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDownloadsBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDownloadList.adapter = downloadsAdapter
        binding.rvDownloadList.addItemDecoration(SpaceItemDecoration())
        binding.splDownloadList.setOnRefreshListener { downloadsAdapter.refresh() }

        observeViewModels()
    }

    private fun observeViewModels() {
        // load the list of pull requests
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            downloadsViewModel.getDownloads().collectLatest { list ->
                downloadsAdapter.submitData(list)
            }
        }

        // observe the loading state and update UI accordingly
        viewLifecycleOwner.lifecycleScope.launch {
            downloadsAdapter.loadStateFlow.collectLatest { loadStates ->
                // get all info for refresh/network indicators
                val hasNetworkTraffic = loadStates.mediator?.append is LoadState.Loading
                val hasNoItems = loadStates.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && downloadsAdapter.itemCount == 0

                // update UI
                isRefreshing(binding.splDownloadList, loadStates.refresh)
                isLoading(binding.pbNetworkActivity, hasNetworkTraffic)
                setVisibilityGone(binding.tvNoDownloads, hasNoItems)

                // check for loading errors
                val combinedLoadStates = listOf(loadStates.append, loadStates.refresh, loadStates.prepend)
                if (combinedLoadStates.any { it is LoadState.Error }) {
                    val message = combinedLoadStates.first { it is LoadState.Error }.toString()
                    showErrorMessage(message)
                }
            }
        }

        // pass the current repository to the other ViewModels
        repositoryViewModel.repository.observe(viewLifecycleOwner, Observer { repository ->
            repository ?: return@Observer
            downloadsViewModel.setSelectedRepositoryUuid(repository.uuid)
        })
    }

    private fun showErrorMessage(errorMessage: String) {
        when {
            errorMessage.isEmpty() -> snackBarPresenter.showSnackBar(getString(R.string.unknown_error))
            else -> snackBarPresenter.showSnackBar(getString(R.string.error_colon, errorMessage))
        }
    }

    override fun onClick(clickedView: View, clickedItem: Any) {
        if (clickedItem !is Download) return

        // ignore clicks on items being downloaded
        val isDownloading = clickedItem.downloadProgress > 0f && clickedItem.downloadProgress < 100f
        if (isDownloading) return


        // Download this file or open it
        val downloadFile = FileHelper.getDownloadFile(clickedItem)
        when (downloadFile.exists()) {
            true -> openFile(downloadFile)
            else -> {
                // log this event
                eventTracker.sendEvent(AnalyticsEvent.SelectContent(ContentType.DOWNLOAD))
                downloadsViewModel.download(clickedItem.id, clickedItem.downloadUrl)
            }
        }
    }

    private fun openFile(downloadFile: File) {
        try {
            val uri = FileProvider.getUriForFile(requireContext(), requireContext().applicationContext.packageName + ".provider", downloadFile)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            snackBarPresenter.showSnackBar(getString(R.string.no_app_found_for_this_file, downloadFile.name))
        }
    }
}