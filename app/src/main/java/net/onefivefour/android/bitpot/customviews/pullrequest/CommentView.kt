package net.onefivefour.android.bitpot.customviews.pullrequest

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.Comment
import net.onefivefour.android.bitpot.data.model.CommentPosition
import net.onefivefour.android.bitpot.databinding.ViewCommentBinding
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min

/**
 * This ViewGroup is showing a comment on a PullRequest.
 * This comment may occur for a whole PR, for a file of this PR or for a single line of code.
 *
 * Comments are sorted hierarchically, because replies ot another comment have a parentId.
 */
class CommentView : ViewGroup {

    private lateinit var binding: ViewCommentBinding

    private var dp1: Int = 0
    private var dp8: Int = 0
    private var dp10: Int = 0
    private var dp16: Int = 0
    private var dp24: Int = 0

    private var hasParentComment = false

    constructor(context: Context) : super(context) {
        initVars(context)
        initRoot(context)
        inflateView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initVars(context)
        initRoot(context)
        inflateView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initVars(context)
        initRoot(context)
        inflateView(context)
    }

    private fun initVars(context: Context) {
        val resources = context.resources
        dp1 = resources.getDimensionPixelSize(R.dimen.dp_1)
        dp8 = resources.getDimensionPixelSize(R.dimen.dp_8)
        dp10 = resources.getDimensionPixelSize(R.dimen.dp_10)
        dp16 = resources.getDimensionPixelSize(R.dimen.dp_16)
        dp24 = resources.getDimensionPixelSize(R.dimen.dp_24)
    }

    private fun initRoot(context: Context) {
        setBackgroundColor(ContextCompat.getColor(context, R.color.color_surface))
        elevation = context.resources.getDimension(R.dimen.dp_2)
    }

    private fun inflateView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewCommentBinding.inflate(inflater, this, true)
    }

    fun setComment(comment: Comment) {
        binding.comment = comment
        hasParentComment = comment.parentId != null
    }

    fun setClickListener(listener: ClickListener) {
        binding.clickListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // measure all children
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            when (child.id) {
                R.id.iv_avatar -> measureAvatar(child)
                R.id.tv_comment -> measureComment(widthMeasureSpec, child)
                else -> measureUnspecified(child)
            }
        }

        // calculate desired width and height
        val resolvedWidth = resolveWidth(widthMeasureSpec)
        val resolvedHeight = resolveHeight(heightMeasureSpec)

        // fulfill measure contract by calling setMeasuredDimensions
        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    private fun resolveHeight(heightMeasureSpec: Int): Int {
        val desiredHeight = dp10 + binding.ivAvatar.measuredHeight + dp8 + binding.tvComment.measuredHeight + dp8 + binding.btnReply.measuredHeight
        return measureDimension(desiredHeight, heightMeasureSpec)
    }

    private fun resolveWidth(widthMeasureSpec: Int): Int {
        val desiredWidthHeaderStrings = max(binding.tvUsername.measuredWidth, binding.tvUpdatedOn.measuredWidth)
        val desiredWidthHeader = dp16 + binding.ivAvatar.measuredWidth + dp8 + desiredWidthHeaderStrings
        val desiredWidthContent = dp16 + binding.tvComment.measuredWidth + dp16
        var desiredWidth = max(desiredWidthHeader, desiredWidthContent)
        if (hasParentComment) {
            // indent reply comments by 24dp
            desiredWidth -= dp24
        }
        return measureDimension(desiredWidth, widthMeasureSpec, true)
    }

    private fun measureUnspecified(child: View) {
        // all other views (i.e. displayName, updatedOn, etc.) do not get restrictions on their size.
        val unspecifiedMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        child.measure(unspecifiedMeasureSpec, unspecifiedMeasureSpec)
    }

    private fun measureComment(widthMeasureSpec: Int, child: View) {
        // the comment text has a 16dp margin left and right.
        // Reply comments are indented 16dp
        // the comment text can be as high as it wants to be.
        var maxWidth = MeasureSpec.getSize(widthMeasureSpec) - dp16 - dp16
        maxWidth -= if (hasParentComment) dp16 else 0
        val commentMeasureSpecWidth = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST)
        val unspecifiedMeasureSpecHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        child.measure(commentMeasureSpecWidth, unspecifiedMeasureSpecHeight)
    }

    private fun measureAvatar(child: View) {
        // the avatar image should be exactly 24 dp in width and height
        val viewMeasureSpecSize = MeasureSpec.makeMeasureSpec(dp24, MeasureSpec.EXACTLY)
        child.measure(viewMeasureSpecSize, viewMeasureSpecSize)
    }

    @Suppress("ComplexMethod")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val indentLeft = if (hasParentComment) dp24 else 0

        // position all views according to what was calculated in onMeasure
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            when (child.id) {
                R.id.iv_avatar -> layoutAvatar(child, indentLeft)
                R.id.tv_username -> layoutUsername(child, indentLeft)
                R.id.tv_updated_on -> layoutUpdatedOn(child, indentLeft)
                R.id.tv_comment -> layoutComment(child, indentLeft)
                R.id.btn_reply -> layoutReplyButton(child, indentLeft)
                R.id.btn_delete -> layoutDeleteButton(child, indentLeft)
                R.id.v_divider -> layoutDivider(child)
                else -> child.layout(0, 0, child.measuredWidth, child.measuredHeight)
            }
        }
    }

    private fun layoutDivider(child: View) {
        child.layout(0, measuredHeight - dp1, measuredWidth, measuredHeight)
    }

    private fun layoutReplyButton(child: View, indentLeft: Int) {
        child.layout(
            indentLeft - dp8,
            measuredHeight - child.measuredHeight - dp8,
            child.measuredWidth + indentLeft - dp8,
            measuredHeight - dp8
        )
    }

    private fun layoutDeleteButton(child: View, indentLeft: Int) {
        child.layout(
            indentLeft - dp8 + binding.btnReply.measuredWidth,
            measuredHeight - child.measuredHeight - dp8,
            indentLeft - dp8 + binding.btnReply.measuredWidth + child.measuredWidth,
            measuredHeight - dp8
        )
    }

    private fun layoutComment(child: View, indentLeft: Int) {
        child.layout(
            dp16 + indentLeft,
            dp10 + binding.ivAvatar.measuredHeight + dp8,
            dp16 + child.measuredWidth + indentLeft,
            dp10 + binding.ivAvatar.measuredHeight + dp8 + child.measuredHeight
        )
    }

    private fun layoutUpdatedOn(child: View, indentLeft: Int) {
        child.layout(
            dp16 + binding.ivAvatar.measuredWidth + dp8 + indentLeft,
            dp8 + binding.tvUsername.measuredHeight,
            dp16 + binding.ivAvatar.measuredWidth + dp8 + child.measuredWidth + indentLeft,
            dp8 + binding.tvUsername.measuredHeight + child.measuredHeight
        )
    }

    private fun layoutUsername(child: View, indentLeft: Int) {
        child.layout(
            dp16 + binding.ivAvatar.measuredWidth + dp8 + indentLeft,
            dp8,
            dp16 + binding.ivAvatar.measuredWidth + dp8 + child.measuredWidth + indentLeft,
            dp8 + child.measuredHeight
        )
    }

    private fun layoutAvatar(child: View, indentLeft: Int) {
        child.layout(
            dp16 + indentLeft,
            dp10,
            dp16 + child.measuredWidth + indentLeft,
            dp10 + child.measuredHeight
        )
    }

    /**
     * figure out the actual size of a View depending on the given measureSpec.
     *
     * @param isWidth width calculation gives maximum width, height calculation gives desired height
     */
    private fun measureDimension(desiredSize: Int, measureSpec: Int, isWidth: Boolean = false): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {

            // width calculation gives maximum width, height calculation gives desired height.
            result = when (isWidth) {
                true -> max(desiredSize, measuredWidth)
                else -> desiredSize
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        if (result < desiredSize) {
            Timber.e("The view is too small, the content might get cut")
        }
        return result
    }

    /**
     * Implement this interface to get notified about clicks within the CommentView.
     */
    interface ClickListener {
        fun onReplyClicked(clickedCommentId: Int, commentPosition: CommentPosition)
        fun onDeleteClicked(clickedCommentId: Int)
    }

}