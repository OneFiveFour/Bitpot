package net.onefivefour.android.bitpot.screens.pullrequests

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.common.SnackBarPresenter
import net.onefivefour.android.bitpot.common.SpaceItemDecoration
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.model.PullRequest
import net.onefivefour.android.bitpot.databinding.FragmentPullRequestsBinding
import net.onefivefour.android.bitpot.databinding.isLoading
import net.onefivefour.android.bitpot.databinding.isRefreshing
import net.onefivefour.android.bitpot.databinding.setVisibilityGone
import net.onefivefour.android.bitpot.screens.repository.RepositoryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * This Fragment shows a list of all pull requests of the selected Repository
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PullRequestsFragment : Fragment(), ItemClickListener {

    private lateinit var binding: FragmentPullRequestsBinding

    private val pullRequestsViewModel: PullRequestsViewModel by viewModel()
    
    private val repositoryViewModel: RepositoryViewModel by sharedViewModel()
    
    private lateinit var snackBarPresenter: SnackBarPresenter

    private val pullRequestsAdapter = PullRequestsAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is SnackBarPresenter -> snackBarPresenter = context
            else -> throw IllegalArgumentException("Given Context must implement SnackBarPresenter interface!")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPullRequestsBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.rvPullRequestList.adapter = pullRequestsAdapter
        binding.rvPullRequestList.addItemDecoration(SpaceItemDecoration())
        binding.splPullRequestList.setOnRefreshListener { pullRequestsAdapter.refresh() }

        observeViewModels()
    }

    private fun observeViewModels() {
        // load the list of pull requests
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            pullRequestsViewModel.getPullRequests().collectLatest { list ->
                pullRequestsAdapter.submitData(list)
            }
        }

        // observe the loading state and update UI accordingly
        viewLifecycleOwner.lifecycleScope.launch {
            pullRequestsAdapter.loadStateFlow.collectLatest { loadStates ->
                // get all info for refresh/network indicators
                val hasNetworkTraffic = loadStates.mediator?.append is LoadState.Loading
                val hasNoItems = loadStates.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && pullRequestsAdapter.itemCount == 0

                // update UI
                isRefreshing(binding.splPullRequestList, loadStates.refresh)
                isLoading(binding.pbNetworkActivity, hasNetworkTraffic)
                setVisibilityGone(binding.tvNoPullRequests, hasNoItems)

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
            pullRequestsViewModel.setSelectedRepositoryUuid(repository.uuid)
        })
    }

    private fun showErrorMessage(errorMessage: String) {
        when {
            errorMessage.isEmpty() -> snackBarPresenter.showSnackBar(getString(R.string.unknown_error))
            else -> snackBarPresenter.showSnackBar(getString(R.string.error_colon, errorMessage))
        }
    }

    override fun onClick(clickedView: View, clickedItem: Any) {
        if (clickedItem !is PullRequest) return

        val transitionName = getString(R.string.transition_name_pull_request)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), clickedView, transitionName)
        val extras = ActivityNavigator.Extras.Builder().setActivityOptions(options).build()
        
        val workspaceUuid = BitpotData.getSelectedWorkspaceUuid() ?: return
        val repositoryUuid = clickedItem.repoUuid
        val pullRequestId = clickedItem.id

        val action = PullRequestsFragmentDirections.actionNavigationPullRequestsToPullRequestActivity(workspaceUuid, repositoryUuid, pullRequestId)
        findNavController().navigate(action, extras)
    }
}