package net.onefivefour.android.bitpot.screens.sources

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.data.model.RefType
import net.onefivefour.android.bitpot.databinding.DialogFragmentRefsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * This DialogFragment shows a ViewPager that allows the user to swipe
 * between different [net.onefivefour.android.bitpot.data.model.RefType]s of the current repository.
 *
 * The UI structure of this DialogFragment is the following:
 *
 * 1. A TabLayout is connected to a horizontal ViewPager2
 * 2. The ViewPager has a [RefsDialogPagerAdapter] to decide what fragment to show in what page
 * 3. Every page is an instance of [RefTypeFragment]
 * 4. Each RefTypeFragment shows a RecyclerView listing all refs of the respective type of the current tab
 *
 */
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class RefsDialogFragment : DialogFragment() {
    
    private lateinit var binding: DialogFragmentRefsBinding

    private val refViewModel: RefViewModel by sharedViewModel()

    companion object {
        const val TAG = "RefSelectionDialogFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogFragmentRefsBinding.inflate(inflater, container, false).also { 
            it.lifecycleOwner = viewLifecycleOwner
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancel.setOnClickListener { dismiss() }
        setupTabLayout()
        observeSelectedRef()
    }

    private fun setupTabLayout() {
        binding.dialogFragmentsRefsViewPager.adapter = RefsDialogPagerAdapter(this)
        TabLayoutMediator(binding.dialogFragmentRefsTabLayout, binding.dialogFragmentsRefsViewPager) { tab, position ->
            tab.text = RefType.values()[position].name
        }.attach()
    }

    private fun observeSelectedRef() {
        refViewModel.hasSelectedRefChanged().observe(viewLifecycleOwner) { hasChanged ->
            if (hasChanged == true) {
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        makeFullScreen()
    }

    /**
     * This method rewrites the dialogs layout params
     * to make it fullscreen by default. A small semi-transparent margin
     * is still to see.
     */
    private fun makeFullScreen() {
        val d: Dialog? = dialog
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            d.window?.setLayout(width, height)
        }
    }
}
