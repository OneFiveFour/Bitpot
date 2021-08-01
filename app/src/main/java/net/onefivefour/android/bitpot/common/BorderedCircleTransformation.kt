package net.onefivefour.android.bitpot.common

import android.graphics.*
import androidx.annotation.ColorInt
import com.squareup.picasso.Transformation
import kotlin.math.min

/**
 * Transforms the image into a circle with a border.
 * The border has the given [borderColor].
 * The border has a width of 10% of the overall width.
 */
class BorderedCircleTransformation(@ColorInt private val borderColor: Int) : Transformation {

    companion object {
        const val CIRCLE_WIDTH_FACTOR = 0.9f
    }

    private val borderPaint = Paint().apply {
        style = Paint.Style.FILL
        color = borderColor
        isAntiAlias = true
    }

    private val clampPaint = Paint().also {
        it.isAntiAlias = true
    }

    override fun transform(source: Bitmap): Bitmap {
        val size = min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val imageSize = (size * CIRCLE_WIDTH_FACTOR).toInt()
        val squaredBitmap = Bitmap.createBitmap(source, x, y, imageSize, imageSize)
        if (squaredBitmap != source) source.recycle()

        val bitmap = Bitmap.createBitmap(size, size, source.config)
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        clampPaint.shader = shader

        val canvas = Canvas(bitmap)

        val smallRadius = size * CIRCLE_WIDTH_FACTOR / 2f
        val bigRadius = size / 2f

        canvas.drawCircle(bigRadius, bigRadius, bigRadius, borderPaint)
        canvas.drawCircle(bigRadius, bigRadius, smallRadius, clampPaint)

        squaredBitmap.recycle()

        return bitmap
    }

    /**
     * Caching String for Picasso.
     * Include the borderColor to prevent caching issues.
     */
    override fun key(): String {
        return "bordered_circle_$borderColor"
    }
}