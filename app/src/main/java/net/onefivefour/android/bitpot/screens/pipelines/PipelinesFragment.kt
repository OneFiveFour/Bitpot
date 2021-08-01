package net.onefivefour.android.bitpot.screens.pipelines

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.model.Pipeline
import net.onefivefour.android.bitpot.databinding.FragmentPipelinesBinding
import net.onefivefour.android.bitpot.databinding.isLoading
import net.onefivefour.android.bitpot.databinding.isRefreshing
import net.onefivefour.android.bitpot.databinding.setVisibilityGone
import net.onefivefour.android.bitpot.screens.repository.RepositoryViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


/**
 * This Fragment shows a list of all pipelines of the selected Repository
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PipelinesFragment : Fragment(), ItemClickListener {
    
    private lateinit var binding: FragmentPipelinesBinding

    private val eventTracker: EventTracker by inject()

    private val pipelinesViewModel: PipelinesViewModel by viewModel()
    
    private val repositoryViewModel: RepositoryViewModel by sharedViewModel()
    
    private lateinit var snackBarPresenter: SnackBarPresenter
    
    private val pipelinesAdapter = PipelinesAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is SnackBarPresenter -> snackBarPresenter = context
            else -> throw IllegalArgumentException("Given Context must implement SnackBarPresenter interface!")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPipelinesBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.rvPipelineList.adapter = pipelinesAdapter
        binding.rvPipelineList.addItemDecoration(SpaceItemDecoration())
        binding.splPipelineList.setOnRefreshListener { pipelinesAdapter.refresh() }

        observeViewModels()
    }

    private fun observeViewModels() {
        // load the list of pipelines
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            pipelinesViewModel.getPipelines().collectLatest { list ->
                pipelinesAdapter.submitData(list)
            }
        }

        // observe the loading state and update UI accordingly
        viewLifecycleOwner.lifecycleScope.launch {
            pipelinesAdapter.loadStateFlow.collectLatest { loadStates ->
                // get all info for refresh/network indicators
                val hasNetworkTraffic = loadStates.mediator?.append is LoadState.Loading
                val hasNoItems = loadStates.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && pipelinesAdapter.itemCount == 0

                // update UI
                isRefreshing(binding.splPipelineList, loadStates.refresh)
                isLoading(binding.pbNetworkActivity, hasNetworkTraffic)
                setVisibilityGone(binding.tvNoPipelines, hasNoItems)

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
            pipelinesViewModel.setSelectedRepositoryUuid(repository.uuid)
        })
    }

    private fun showErrorMessage(errorMessage: String) {
        when {
            errorMessage.isEmpty() -> snackBarPresenter.showSnackBar(getString(R.string.unknown_error))
            else -> snackBarPresenter.showSnackBar(getString(R.string.error_colon, errorMessage))
        }
    }
    
    override fun onClick(clickedView: View, clickedItem: Any) {
        if (clickedItem !is Pipeline) return
        Timber.d("+++ Pipeline clicked: ${clickedItem.buildNumber}")
        val workspaceUuid = BitpotData.getSelectedWorkspaceUuid() ?: return
        val url = getString(R.string.pipeline_url, workspaceUuid, clickedItem.repoUuid, clickedItem.buildNumber)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)

        // log this event
        eventTracker.sendEvent(AnalyticsEvent.SelectContent(ContentType.PIPELINE))
    }
}