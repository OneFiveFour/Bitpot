package net.onefivefour.android.bitpot.customviews.participants

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.common.BorderedCircleTransformation
import net.onefivefour.android.bitpot.data.model.Participant
import net.onefivefour.android.bitpot.extensions.toBitmap

/**
 * This View shows the avatar image of a participant along with optional content:
 * If the participant has approved the PR, there is a green check mark rendered on the bottom right.
 * If this View is set to be a number View, it is just a grey circle with the given number of
 * additional (hidden) participants.
 */
class ParticipantView : View {

    companion object {
        const val CHECK_MARK_ORIGIN_PERCENTAGE = 0.66f
    }

    /**
     * The Paint of the grey circle.
     * Used only if this View is set to be a number View.
     */
    private val circlePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GRAY
        isAntiAlias = true
    }

    /**
     * The text pain of the number.
     * Used only if this View is set to be a number View.
     */
    private val numberPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        isAntiAlias = true
        textSize = resources.getDimension(R.dimen.number_text_size)
        textAlign = Paint.Align.CENTER
    }

    /**
     * The drawing Rect of the avatar.
     * Spans the whole View as soon as we have measurements.
     */
    private var destinationRectAvatar = RectF(0f, 0f, 0f, 0f)

    /**
     * The drawing Rect for the green check mark.
     * Spans the bottom right corner as soon as we have measurements.
     */
    private var destinationRectCheckMark = RectF(0f, 0f, 0f, 0f)

    /**
     * The data of the Participant to show.
     */
    private lateinit var participant: Participant

    /**
     * The number of hidden Participants.
     * Used only if this View is set to be a number View.
     */
    private var number = 0

    /**
     * The avatar bitmap of the Participant.
     */
    private var avatar: Bitmap? = null

    /**
     * The bitmap of the green check mark.
     */
    private val checkMark = ContextCompat.getDrawable(context, R.drawable.ic_check_mark)?.toBitmap()

    /**
     * Strong reference for Picasso to load the avatar image from the
     * Bitbucket server.
     */
    private val bitmapTarget: Target = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) { /* do nothing */ }
        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) { /* do nothing */ }
        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            setBitmap(bitmap)
        }
    }

    /**
     * Called by Picasso as soon as it has loaded the bitmap.
     */
    private fun setBitmap(bitmap: Bitmap?) {
        this.avatar = bitmap
        invalidate()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, participant: Participant, @ColorInt borderColor: Int) : super(context) {
        this.participant = participant
        Picasso.get()
            .load(participant.avatarUrl)
            .transform(BorderedCircleTransformation(borderColor))
            .into(bitmapTarget)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val checkMarkTop = height * CHECK_MARK_ORIGIN_PERCENTAGE
        val checkMarkLeft = width * CHECK_MARK_ORIGIN_PERCENTAGE

        destinationRectAvatar = RectF(0f, 0f, width.toFloat(), height.toFloat())
        destinationRectCheckMark = RectF(checkMarkLeft, checkMarkTop, width.toFloat(), height.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when {
            number > 0 -> drawNumber(canvas)
            else -> drawAvatar(canvas)
        }
    }

    /**
     * Draw a grey circle with the number of hidden Participant written in it
     */
    private fun drawNumber(canvas: Canvas) {
        val centerX = width / 2f
        val centerY = height / 2f
        val textCenterY = canvas.height / 2 - (numberPaint.descent() + numberPaint.ascent()) / 2

        canvas.drawCircle(centerX, centerY, centerX, circlePaint)
        canvas.drawText(" +$number", centerX, textCenterY, numberPaint)
    }

    /**
     * Draw the avatar bitmap and optionally the green check mark bitmap.
     */
    private fun drawAvatar(canvas: Canvas) {
        if (avatar != null) {
            canvas.drawBitmap(avatar!!, null, destinationRectAvatar, null)
        }

        if (this::participant.isInitialized && participant.hasApproved && checkMark != null) {
            canvas.drawBitmap(checkMark, null, destinationRectCheckMark, null)
        }
    }

    /**
     * Set this View to be a grey circle with the given number written in it.
     */
    fun setToNumber(number: Int) {
        this.number = number
    }
}