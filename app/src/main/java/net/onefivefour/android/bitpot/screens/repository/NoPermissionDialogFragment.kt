package net.onefivefour.android.bitpot.screens.repository

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.common.WebHookData
import net.onefivefour.android.bitpot.databinding.DialogNoPermissionBinding


/**
 * This AlertDialog is used to inform the user about his/her options when trying
 * to enable a notification and it turns out that he/her has no admin privileges.
 */
class NoPermissionDialogFragment : DialogFragment()  {
    
    private lateinit var binding: DialogNoPermissionBinding

    companion object {
        const val TAG = "NoPermissionDialogFragment"
    }

    private enum class State { ALL_COLLAPSED, OPTION_1, OPTION_2, OPTION_3 }

    private var currentTargetState = State.ALL_COLLAPSED

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogNoPermissionBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvWebHookUrl.text = WebHookData.getUrl()
        binding.btnOk.setOnClickListener { dismiss() }
        binding.btnCopy.setOnClickListener { copyWebHookUrl() }
        binding.btnShare.setOnClickListener { shareWebHookUrl() }

        binding.dialogNoPermissionOption1Title.setOnClickListener { toggleState(State.OPTION_1) }
        binding.dialogNoPermissionOption2Title.setOnClickListener { toggleState(State.OPTION_2) }
        binding.dialogNoPermissionOption3Title.setOnClickListener { toggleState(State.OPTION_3) }
    }

    override fun onStart() {
        super.onStart()
        makeFullScreen()
    }

    private fun toggleState(targetState: State) {
        currentTargetState = when (currentTargetState) {
            targetState -> State.ALL_COLLAPSED
            else -> targetState
        }

        when (currentTargetState) {
            State.ALL_COLLAPSED -> {
                binding.dialogNoPermissionOption1Text.collapse()
                binding.dialogNoPermissionOption2Text.collapse()
                binding.dialogNoPermissionOption3Text.collapse()

            }
            State.OPTION_1 -> {
                binding.dialogNoPermissionOption1Text.expand()
                binding.dialogNoPermissionOption2Text.collapse()
                binding.dialogNoPermissionOption3Text.collapse()

            }
            State.OPTION_2 -> {
                binding.dialogNoPermissionOption1Text.collapse()
                binding.dialogNoPermissionOption2Text.expand()
                binding.dialogNoPermissionOption3Text.collapse()


            }
            State.OPTION_3 -> {
                binding.dialogNoPermissionOption1Text.collapse()
                binding.dialogNoPermissionOption2Text.collapse()
                binding.dialogNoPermissionOption3Text.expand()

            }
        }
    }

    private fun shareWebHookUrl() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, WebHookData.getUrl())
            type = "text/plain"
        }

        val title = getString(R.string.web_hook_url)
        val shareIntent = Intent.createChooser(sendIntent, title)
        startActivity(shareIntent)
    }

    private fun copyWebHookUrl() {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val label = getString(R.string.web_hook_url)
        val clip: ClipData = ClipData.newPlainText(label, WebHookData.getUrl())
        clipboardManager.setPrimaryClip(clip)

        Snackbar.make(requireView(), R.string.web_hook_url_copied_to_clipboard, Snackbar.LENGTH_LONG).show()
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