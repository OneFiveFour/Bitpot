package net.onefivefour.android.bitpot.screens.repositories


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import kotlinx.coroutines.flow.collectLatest
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.common.SpaceItemDecoration
import net.onefivefour.android.bitpot.data.model.Repository
import net.onefivefour.android.bitpot.databinding.FragmentRepositoriesBinding
import net.onefivefour.android.bitpot.databinding.isLoading
import net.onefivefour.android.bitpot.databinding.setVisibilityGone
import net.onefivefour.android.bitpot.screens.main.WorkspacesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * This fragment shows a list of the users Bitbucket Repositories.
 * Most of the UI is populated via data binding and the Android Paging Library Version 3.
 */
@ExperimentalPagingApi
class RepositoriesFragment : Fragment(), ItemClickListener {
    
    private lateinit var binding: FragmentRepositoriesBinding

    private val repositoriesViewModel: RepositoriesViewModel by viewModel()
    
    private val workspacesViewModel: WorkspacesViewModel by sharedViewModel()
    
    private val adapter = RepositoriesAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding =  FragmentRepositoriesBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = repositoriesViewModel
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRepositoryList.adapter = adapter
        binding.rvRepositoryList.addItemDecoration(SpaceItemDecoration())

        binding.splRepositoryList.setOnRefreshListener { adapter.refresh() }

        // update the UI according to refresh/append loading states
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                val isAppending = loadStates.mediator?.append is LoadState.Loading
                isLoading(binding.pbNetworkActivity, isAppending)
                binding.splRepositoryList.isRefreshing = loadStates.refresh is LoadState.Loading

                val isEmpty = loadStates.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && adapter.itemCount < 1
                setVisibilityGone(binding.tvNoRepositories, isEmpty)
            }
        }

        // load the list of repositories
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            repositoriesViewModel.repositories.collectLatest { list ->
                adapter.submitData(lifecycle, list)
            }
        }
        
        workspacesViewModel.selectedWorkspace.observe(viewLifecycleOwner) {
            // The selected workspace changed -> refresh the list of repositories.
            adapter.refresh()
        }
    }

    override fun onClick(clickedView: View, clickedItem: Any) {
        if (clickedItem !is Repository) return

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity())
        val extras = ActivityNavigatorExtras(options)
        val action = RepositoriesFragmentDirections.actionRepositoriesFragmentToRepositoryActivity(clickedItem.workspaceUuid, clickedItem.uuid)
        findNavController().navigate(action, extras)
    }
}
