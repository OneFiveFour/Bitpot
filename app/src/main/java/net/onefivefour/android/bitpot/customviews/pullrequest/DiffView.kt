package net.onefivefour.android.bitpot.customviews.pullrequest

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.Comment
import net.onefivefour.android.bitpot.data.model.CommentPosition
import net.onefivefour.android.bitpot.data.model.DiffType
import net.onefivefour.android.bitpot.data.model.FileDiff
import net.onefivefour.android.bitpot.databinding.ViewDiffBinding
import net.onefivefour.android.bitpot.extensions.getThemeColor


/**
 * This View allows an easy visual representation of a [FileDiff]
 */
class DiffView : ConstraintLayout, CommentView.ClickListener {

    private lateinit var commentCallback: CommentCallback
    
    private lateinit var binding : ViewDiffBinding

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
        inflateView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
        inflateView(context)
    }

    private fun initView(context: Context) {
        background = ContextCompat.getDrawable(context, R.drawable.shape_rounded_rect_10dp)
        backgroundTintList = ColorStateList.valueOf(context.getThemeColor(R.attr.colorSurface))
        elevation = context.resources.getDimension(R.dimen.dp_2)
        clipToOutline = true
    }

    private fun inflateView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewDiffBinding.inflate(inflater, this, true)
    }

    fun setDiff(fileDiff: FileDiff) {

        // set the file path in the UI
        setFilePath(fileDiff)

        // set clickListener to add a file comment
        binding.ivAddFileComment.setOnClickListener {
            commentCallback.onCreateComment(null, CommentPosition.File(fileDiff.filePath.toString()))
        }

        // set the correct type icon for this file
        setDiffType(fileDiff)

        // hide the content container if there are no lines of code to show
        showOrHideContent(fileDiff)

        // clear the LinearLayout
        binding.llDiffContent.removeAllViews()

        // the width of the line number area is the same for each line.
        // That's why we are pre-calculating it here once and re-use it in the loop.
        val lineNumbersPaint= getLineNumberPaint()
        val lineNumbersWidth = getLineNumberWidth(lineNumbersPaint, fileDiff)

        // Add PullRequest comments.
        addPullRequestComments(fileDiff)

        // Add comments for files or lines of code.
        addFileAndLineComments(fileDiff, lineNumbersPaint, lineNumbersWidth)
    }

    /**
     * Iterate over every line of this diff and add it to the LinearLayout.
     * Also check if each line has comments and add them too, if any.
     */
    private fun addFileAndLineComments(fileDiff: FileDiff, lineNumbersPaint: Paint, lineNumbersWidth: Float) {
        for (diffLine in fileDiff.code) {
            val textView = DiffLineView(context)
            textView.setLineNumberInfo(lineNumbersPaint, lineNumbersWidth)
            textView.setContent(diffLine.content)
            textView.setDiffLineType(diffLine.type)
            textView.setOnClickListener {
                val position = CommentPosition.Line(diffLine.sourceLineNumber, diffLine.destinationLineNumber, fileDiff.filePath.toString())
                createComment(null, position)
            }
            binding.llDiffContent.addView(textView)

            // if there are no comments on the current one, we can go to the next line
            if (diffLine.comments.isEmpty()) {
                continue
            }

            // Otherwise, sort the comments and add them to the UI
            diffLine.comments.forEach { comment -> addComment(comment) }
        }
    }

    private fun addPullRequestComments(fileDiff: FileDiff) {
        fileDiff.comments.forEach { comment -> addComment(comment) }
    }

    private fun getLineNumberWidth(lineNumbersPaint: Paint, fileDiff: FileDiff) =
        lineNumbersPaint.measureText("".padEnd(fileDiff.lineNumberStringLength))

    private fun getLineNumberPaint(): Paint {
        return Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.dlv_color_line_number)
            typeface = Typeface.MONOSPACE
            textSize = context.resources.getDimension(R.dimen.dlv_text_size)
        }
    }

    private fun showOrHideContent(fileDiff: FileDiff) {
        binding.svContentScroller.visibility = if (fileDiff.code.isEmpty() && fileDiff.comments.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun setDiffType(fileDiff: FileDiff) {
        val drawableRes = when (fileDiff.type) {
            DiffType.FILE_ADDED -> R.drawable.ic_file_added
            DiffType.FILE_REMOVED -> R.drawable.ic_file_removed
            DiffType.FILE_MODIFIED -> R.drawable.ic_file_modified
        }
        binding.ivFileType.setImageResource(drawableRes)
    }

    private fun setFilePath(fileDiff: FileDiff) {
        binding.tvFilePath.text = fileDiff.filePath
    }

    private fun addComment(comment: Comment) {
        val commentView = CommentView(context)
        commentView.setComment(comment)
        commentView.setClickListener(this)
        binding.llDiffContent.addView(commentView)
    }

    fun setCommentCallback(callback: CommentCallback) {
        this.commentCallback = callback
    }

    override fun onReplyClicked(clickedCommentId: Int, commentPosition: CommentPosition) {
        createComment(clickedCommentId, commentPosition)
    }

    override fun onDeleteClicked(clickedCommentId: Int) {
        commentCallback.onDeleteComment(clickedCommentId)
    }

    private fun createComment(clickedCommentId: Int?, commentPosition: CommentPosition) {
        commentCallback.onCreateComment(clickedCommentId, commentPosition)
    }

    /**
     * Implement this interface to get notified about events within the DiffView.
     */
    interface CommentCallback {
        fun onCreateComment(parentId: Int?, commentPosition: CommentPosition)
        fun onDeleteComment(clickedCommentId: Int)
    }
}



