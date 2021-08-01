package net.onefivefour.android.bitpot.customviews.participants

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import net.onefivefour.android.bitpot.data.model.Participant

/**
 * This Layout renders all [Participant] in an overlapping manner side-by-side.
 */
class ParticipantsListLayout : ViewGroup {

    companion object {
        const val OVERLAPPING_PERCENTAGE = 0.8f
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * The given list of Participants will be rendered side-by-side, overlapping
     * each other a bit. The participants will be ordered by "hasApproved"-first.
     * There will also be a border around each Participant in the given borderColor
     */
    fun setParticipants(participants: List<Participant>, @ColorInt borderColor: Int) {
        removeAllViews()
        participants.sortedBy { if (it.hasApproved) 1 else -1 }.forEach {
            val participantView = ParticipantView(context, it, borderColor)
            addView(participantView)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        // make all children square to the same length as the parents height.
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(heightMeasureSpec, heightMeasureSpec)
        }

        // keep the given measurements for this parent.
        val measuredW = MeasureSpec.getSize(widthMeasureSpec)
        val measuredH = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(measuredW, measuredH)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        // Since all children are square, measuredHeight is equivalent to their edge size.
        // We want the children to overlap after 80% of their edge size.
        val xStep = measuredHeight * OVERLAPPING_PERCENTAGE

        // In case there is not enough room to display all Participants,
        // we are hiding the remaining ones.
        var hiddenViews = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            // Calculate the x position of this child
            // starting from last to first item. This is done, because
            // we can then first count invisible items (see next step)
            val newX = xStep * (childCount - i - 1)

            // Would this child be fully visible?
            if (newX + measuredHeight > measuredWidth) {
                // no, make it invisible and increase the number of hidden views.
                child.visibility = View.GONE
                hiddenViews++
                continue

            } else if (hiddenViews > 0) {
                // yes AND we have hiddenViews.
                // account for the current child, too
                hiddenViews++

                // set the current child as the numbered item
                (child as ParticipantView).setToNumber(hiddenViews)

                // reset hiddenViews to prevent triggering this else-path again.
                hiddenViews = 0
            }

            // position the current child.
            val left = newX.toInt()
            val bottom = measuredHeight
            val right = newX.toInt() + measuredHeight
            child.layout(left, 0, right, bottom)
        }
    }

}
