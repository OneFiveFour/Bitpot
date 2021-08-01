package net.onefivefour.android.bitpot.screens.pullrequest

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import com.google.android.material.textfield.TextInputLayout
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.MergeStrategy
import net.onefivefour.android.bitpot.data.model.PostMerge

/**
 * This AlertDialog is shown before a pull request is merged.
 * The user can enter a commit message, select the merge strategy and
 * define whether the branch should be closed after merging.
 */
class MergeDialog(
    context: Context,
    private val mergeCommand: (merge: PostMerge) -> Unit
) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_merge_pull_request)

        val commitMessage: EditText? = findViewById<TextInputLayout>(R.id.et_commit_message).editText
        val mergeButton: Button = findViewById(R.id.btn_merge_pull_request)
        val cancelButton: Button = findViewById(R.id.btn_cancel)

        mergeButton.setOnClickListener {
            val postMerge = createPostMerge()
            mergeCommand.invoke(postMerge)
            dismiss()
            closeKeyboard()
        }

        cancelButton.setOnClickListener {
            dismiss()
            closeKeyboard()
        }

        commitMessage?.requestFocus()
        openKeyboard()
    }

    private fun createPostMerge(): PostMerge {
        val commitMessage: String = findViewById<TextInputLayout>(R.id.et_commit_message).editText?.text.toString()
        val closeSourceBranch = findViewById<CheckBox>(R.id.cb_close_source_branch).isChecked
        val selectedMergeStrategy = findViewById<Spinner>(R.id.sp_merge_strategy).selectedItem as String
        val mergeStrategy = when (selectedMergeStrategy) {
            context.getString(R.string.merge_commit) -> MergeStrategy.MERGE_COMMIT
            context.getString(R.string.squash) -> MergeStrategy.SQUASH
            context.getString(R.string.fast_forward) -> MergeStrategy.FAST_FORWARD
            else -> throw IllegalStateException("Unknown merge strategy selected: $selectedMergeStrategy")
        }

        return PostMerge(commitMessage, closeSourceBranch, mergeStrategy)
    }

    private fun openKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun closeKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}