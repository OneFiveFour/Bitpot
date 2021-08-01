package net.onefivefour.android.bitpot.screens.pullrequest

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.CommentPosition
import net.onefivefour.android.bitpot.data.model.PostComment

/**
 * This AlertDialog is used to create a new pull request comment.
 */
class CommentDialog(
    context: Context?,
    private val parentId: Int?,
    private val commentPosition: CommentPosition,
    private val sendCommand: (comment: PostComment) -> Unit
) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_write_comment)

        val editText: EditText = findViewById(R.id.et_add_comment)
        val sendButton: Button = findViewById(R.id.btn_send_comment)
        val cancelButton: Button = findViewById(R.id.btn_cancel)

        sendButton.setOnClickListener {
            val comment = editText.text.toString()
            val newComment = PostComment(comment, parentId, commentPosition)

            sendCommand.invoke(newComment)

            dismiss()
            closeKeyboard()
        }

        cancelButton.setOnClickListener {
            dismiss()
            closeKeyboard()
        }

        editText.requestFocus()

        openKeyboard()
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