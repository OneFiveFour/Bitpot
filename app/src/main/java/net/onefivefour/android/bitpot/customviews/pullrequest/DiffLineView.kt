package net.onefivefour.android.bitpot.customviews.pullrequest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.util.AttributeSet
import android.view.View
import com.facebook.fbui.textlayoutbuilder.TextLayoutBuilder
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.DiffLineType
import net.onefivefour.android.bitpot.extensions.getThemeColor
import kotlin.math.max

/**
 * This custom View was created to allow fast rendering of a single line
 * of a Diff. Since there may be dozens of lines for each Diff this must happens
 * as fast as possible, which is achieved by several actions:
 *
 * This View is not extending TextView. Instead it creates its own StaticLayout using a [TextLayoutBuilder].
 * The generated [Layout] is then used in the onDraw() method to render the text
 *
 * Since this View will be generated for each line in a diff, we can pre-calculate several
 * parameters outside of the generation loop. That is the reason why this class has many late init that
 * are set by public methods. See [DiffView] for usage details.
 */
class DiffLineView : View {

    private lateinit var lineType: DiffLineType

    private lateinit var lineNumbersPaint: Paint
    private var lineNumbersWidth = 0f

    private lateinit var textLayoutBuilder : TextLayoutBuilder

    private lateinit var staticLayout: Layout

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    /**
     * Call this method to set all information to render the background of line numbers.
     *
     * @param paint the Paint that should be used to render the background of the line numbers
     * @param width the measured width of the line number text
     */
    fun setLineNumberInfo(paint: Paint, width: Float) {
        lineNumbersPaint = paint
        lineNumbersWidth = width
    }

    /**
     * Call this method to set the text content of the diff line
     */
    fun setContent(content: String) {
        this.textLayoutBuilder = TextLayoutBuilder().apply {
            text = if (content.isEmpty()) " " else content
            singleLine = true
            textColor = context.getThemeColor(R.attr.colorOnSurface)
            typeface = Typeface.MONOSPACE
            setTextSize(context.resources.getDimensionPixelSize(R.dimen.dlv_text_size))
        }
    }

    /**
     * Call this method to set the [DiffLineType] of the line.
     * It is mainly used to define the background color of the View.
     */
    fun setDiffLineType(diffLineType: DiffLineType) {
        lineType = diffLineType
        val bgColorRes = when (diffLineType) {
            DiffLineType.LINE_ADDED -> R.color.dlv_color_line_added
            DiffLineType.LINE_REMOVED -> R.color.dlv_color_line_removed
            DiffLineType.CONFLICT_DELIMITER -> R.color.dlv_color_line_conflict_delimiter
            DiffLineType.LINE_CONFLICT -> R.color.dlv_color_line_conflict
            DiffLineType.CHUNK_HEADER -> R.color.dlv_color_chunk_header
            DiffLineType.LINE_NEUTRAL -> R.color.color_surface
            DiffLineType.LINE_NUMBERLESS -> R.color.color_surface
        }

        setBackgroundResource(bgColorRes)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // set the width
        val widthRequirement = MeasureSpec.getSize(widthMeasureSpec)
        textLayoutBuilder.setWidth(widthRequirement, TextLayoutBuilder.MEASURE_MODE_UNSPECIFIED)

        // create static layout to get width and height
        staticLayout = textLayoutBuilder.build() ?: throw IllegalStateException("Can't build StaticLayout.")
        val width = max(staticLayout.width, widthRequirement)
        setMeasuredDimension(width, staticLayout.height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // draw background for line numbers
        if (lineType != DiffLineType.CHUNK_HEADER) {
            canvas.drawRect(0f, 0f, lineNumbersWidth, measuredHeight.toFloat(), lineNumbersPaint)
        }

        // draw the text on the canvas after adjusting for padding
        canvas.save()
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())

        if (this::staticLayout.isInitialized) {
            staticLayout.draw(canvas)
        }

        canvas.restore()
    }
}