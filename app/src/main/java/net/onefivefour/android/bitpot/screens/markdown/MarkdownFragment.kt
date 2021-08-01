package net.onefivefour.android.bitpot.screens.markdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import net.onefivefour.android.bitpot.databinding.FragmentMarkdownBinding
import net.onefivefour.android.bitpot.databinding.setMarkDown
import net.onefivefour.android.bitpot.extensions.readFileFromAssets


/**
 * A simple Fragment to display hard-coded markdown content.
 * To use this Fragment, place the md-file in the assets/markdown/ folder and
 * pass its filename as navigation argument to this fragment.
 */
class MarkdownFragment : Fragment() {
    
    private lateinit var binding : FragmentMarkdownBinding

    private val args: MarkdownFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMarkdownBinding.inflate(inflater, container, false)        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = requireContext().readFileFromAssets("markdown/${args.fileName}")
        setMarkDown(binding.tvMarkdown, content)
    }
}