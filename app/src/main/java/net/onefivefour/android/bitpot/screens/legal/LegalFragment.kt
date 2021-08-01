package net.onefivefour.android.bitpot.screens.legal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.onefivefour.android.bitpot.common.ItemClickListener
import net.onefivefour.android.bitpot.common.SpaceItemDecoration
import net.onefivefour.android.bitpot.data.model.LegalItem
import net.onefivefour.android.bitpot.databinding.FragmentLegalBinding
import net.onefivefour.android.bitpot.screens.markdown.MarkdownFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * This fragment lists all items regarding legal stuff.
 * Privacy policy, imprint and so on
 */
class LegalFragment : Fragment(), ItemClickListener {

    private lateinit var binding: FragmentLegalBinding

    private val legalViewModel: LegalViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLegalBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = legalViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvLegal.adapter = LegalItemsAdapter(this)
        binding.rvLegal.addItemDecoration(SpaceItemDecoration())
    }

    override fun onClick(clickedView: View, clickedItem: Any) {
        if (clickedItem !is LegalItem) return
        Timber.d("+++ clicked on LegalItem: ${getString(clickedItem.titleRes)}")

        val title = getString(clickedItem.titleRes)
        val action = MarkdownFragmentDirections.actionGlobalFragmentMarkdown(clickedItem.fileName, title)
        findNavController().navigate(action)
    }

}