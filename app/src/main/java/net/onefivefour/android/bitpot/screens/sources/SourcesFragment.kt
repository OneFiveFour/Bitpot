package net.onefivefour.android.bitpot.screens.sources

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.common.SnackBarPresenter
import net.onefivefour.android.bitpot.data.model.Source
import net.onefivefour.android.bitpot.data.model.SourceType
import net.onefivefour.android.bitpot.databinding.FragmentSourcesBinding
import net.onefivefour.android.bitpot.databinding.isLoading
import net.onefivefour.android.bitpot.databinding.isRefreshing
import net.onefivefour.android.bitpot.databinding.setVisibilityGone
import net.onefivefour.android.bitpot.screens.repository.RepositoryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


/**
 * This Fragment shows a list of all sources of the selected Repository
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class SourcesFragment : Fragment(), ItemClickListener {

    private lateinit var binding: FragmentSourcesBinding
    
    private val repositoryViewModel: RepositoryViewModel by sharedViewModel()

    private val sourcesViewModel: SourcesViewModel by sharedViewModel()
    
    private val refViewModel: RefViewModel by sharedViewModel()

    private val sourcesAdapter = SourcesAdapter(this)
    
    private lateinit var snackBarPresenter: SnackBarPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is SnackBarPresenter -> snackBarPresenter = context
            else -> throw IllegalArgumentException("Given Context must implement SnackBarPresenter interface!")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSourcesBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.sourcesViewModel = sourcesViewModel
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSourceList.adapter = sourcesAdapter

        observeViewModels()
        setupListeners()
    }
    
    private fun setupListeners() {
        binding.splSourceList.setOnRefreshListener { sourcesAdapter.refresh() }
        binding.btnSelectBranch.setOnClickListener { openRefSelection() }
        binding.btnSelectBranch.setOnLongClickListener { toastBranchName() }
    }

    private fun observeViewModels() {
        // load the list of sources
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sourcesViewModel.getSources().collectLatest { list ->
                sourcesAdapter.submitData(list)
            }
        }

        // observe the loading state and update UI accordingly
        viewLifecycleOwner.lifecycleScope.launch {
            sourcesAdapter.loadStateFlow.collectLatest { loadStates ->
                // get all info for refresh/network indicators
                val hasNetworkTraffic = loadStates.mediator?.append is LoadState.Loading
                val hasNoItems = loadStates.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && sourcesAdapter.itemCount == 0

                // update UI
                isRefreshing(binding.splSourceList, loadStates.refresh)
                isLoading(binding.pbNetworkActivity, hasNetworkTraffic)
                setVisibilityGone(binding.tvNoSources, hasNoItems)

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
            if (repository == null) return@Observer
            sourcesViewModel.setRepository(repository)
            refViewModel.setRepositoryUuid(repository.uuid)
        })

        // observe the selected branch/tag and pass it into the sourcesViewModel
        refViewModel.getSelectedRef().observe(viewLifecycleOwner) { selectedRef ->
            selectedRef ?: return@observe
            sourcesViewModel.setSelectedRef(selectedRef)
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        when {
            errorMessage.isEmpty() -> snackBarPresenter.showSnackBar(getString(R.string.unknown_error))
            else -> snackBarPresenter.showSnackBar(getString(R.string.error_colon, errorMessage))
        }
    }

    private fun openRefSelection() {
        val dialogFragment = RefsDialogFragment()
        dialogFragment.show(childFragmentManager, RefsDialogFragment.TAG)
    }

    override fun onClick(clickedView: View, clickedItem: Any) {
        if (clickedItem !is Source) return

        when (clickedItem.type) {
            SourceType.DIRECTORY -> {
                // if clickedItem.path is not the (empty) root directory, join this path with the clicked directory name using '/'
                val directoryPath = if (clickedItem.path.isEmpty()) clickedItem.name else "${clickedItem.path}/${clickedItem.name}"
                sourcesViewModel.setSelectedPath(directoryPath)
            }
            SourceType.FOLDER_UP -> sourcesViewModel.navigateUp()
            SourceType.FILE -> openFileActivity(clickedItem)
            SourceType.UNKNOWN -> Timber.e("=== click on unknown file type registered: ${clickedItem.path}/${clickedItem.name}")
        }
    }

    private fun openFileActivity(clickedItem: Source) {
        val action = SourcesFragmentDirections.actionNavigationSourcesToFileActivity(
            clickedItem.refName,
            clickedItem.path,
            clickedItem.name
        )
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity())
        val extras = ActivityNavigatorExtras(options)
        findNavController().navigate(action, extras)
    }

    private fun toastBranchName(): Boolean {
        Toast.makeText(context, binding.btnSelectBranch.text, Toast.LENGTH_SHORT).show()
        return true
    }
}