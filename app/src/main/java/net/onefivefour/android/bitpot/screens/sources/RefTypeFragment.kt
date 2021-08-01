package net.onefivefour.android.bitpot.screens.sources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.data.model.Ref
import net.onefivefour.android.bitpot.data.model.RefType
import net.onefivefour.android.bitpot.databinding.FragmentRefTypeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * This fragment lists all [Ref]s of a single [RefType] of
 * a single [net.onefivefour.android.bitpot.data.model.Repository]. The user can for example click on
 * a branch in this list to select it and show all sources
 * of this branch.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class RefTypeFragment : Fragment(), ItemClickListener {
    
    private lateinit var binding : FragmentRefTypeBinding

    companion object {
        const val ARG_REF_TYPE_ORDINAL = "ARG_REF_TYPE_ORDINAL"
    }

    private val refViewModel: RefViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRefTypeBinding.inflate(inflater,container,false).also { 
            it.lifecycleOwner = viewLifecycleOwner
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRefs.adapter = RefsAdapter(this)

        arguments
            ?.takeIf { it.containsKey(ARG_REF_TYPE_ORDINAL) }
            ?.apply {
                val refTypeOrdinal = getInt(ARG_REF_TYPE_ORDINAL)
                observeViewModel(RefType.values()[refTypeOrdinal])
            }
    }

    private fun observeViewModel(refType: RefType) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            when (refType) {
                RefType.BRANCH -> refViewModel.branches
                RefType.TAG -> refViewModel.tags
                RefType.ALL -> refViewModel.all
            }.collectLatest { refs ->
                (binding.rvRefs.adapter as RefsAdapter).submitData(refs)
            }
        }
    }

    override fun onClick(clickedView: View, clickedItem: Any) {
        if (clickedItem !is Ref) return
        refViewModel.setSelectedRef(clickedItem)
    }
}