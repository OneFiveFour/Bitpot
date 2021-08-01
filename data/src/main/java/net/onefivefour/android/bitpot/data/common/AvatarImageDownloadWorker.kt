package net.onefivefour.android.bitpot.data.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import androidx.work.*
import net.onefivefour.android.bitpot.data.database.RepositoryColorsDao
import net.onefivefour.android.bitpot.data.model.RepositoryColors
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.io.IOException
import java.net.URL

/**
 * Giving a set of bitmap URLs with their according repository Uuid, this Worker is responsible for
 * downloading these bitmaps, extract the [RepositoryColors] out of them and store everything in the database.
 */
internal class AvatarImageDownloadWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val repositoryColorsDao: RepositoryColorsDao by inject()

    /**
     * The input data for this worker which is a list of repository uuid with their respective image Url of their avatar image.
     */
    private val repositoryUuidToBitmapUrl = inputData.keyValueMap


    /**
     * Download all avatar images and extract [RepositoryColors] out of them.
     * These colors are then saved into the database.
     */
    override suspend fun doWork(): Result {
        if (repositoryUuidToBitmapUrl.isEmpty()) return Result.failure()

        for ((repositoryUuid, bitmapUrl) in repositoryUuidToBitmapUrl) {
            val bitmap = downloadBitmap(bitmapUrl as String) ?: continue
            calculateColors(bitmap, repositoryUuid)
        }

        return Result.success()
    }

    /**
     * Download the given bitmap url and convert it into a [Bitmap]
     */
    private fun downloadBitmap(bitmapUrl: String): Bitmap? {
        return try {
            val url = URL(bitmapUrl)
            BitmapFactory.decodeStream(url.openStream())
        } catch (e: IOException) {
            Timber.e("Could not download avatar from URL: $repositoryUuidToBitmapUrl: $e")
            null
        }
    }

    /**
     * Use the given Bitmap to asynchronously create a Palette.
     * Using this palette, this method extracts [RepositoryColors] and stores them in the database
     * together with the given repositoryUuid.
     */
    private fun calculateColors(bitmap: Bitmap, repositoryUuid: String) {

        val palette = Palette.from(bitmap).generate()

        val swatch = palette.dominantSwatch
        val fromColor = swatch?.rgb ?: palette.getLightVibrantColor(0x49b985)
        var toColor = fromColor
        val textColor: Int

        if (swatch == null) {
            // Swatch could not be found. Calculate text simple values
            val background = ColorUtils.setAlphaComponent(fromColor, 255) // ensure non-translucent background
            var alpha = ColorUtils.calculateMinimumAlpha(Color.WHITE, background, 4.5f)
            if (alpha == -1) alpha = 255 // ensure valid alpha value
            textColor = ColorUtils.setAlphaComponent(Color.WHITE, alpha)

        } else {
            // Swatch was found. Calculate text values. Ignore alpha values by bit shift left 8
            val blendColor = when (swatch.bodyTextColor shl 8) {
                Color.WHITE shl 8 -> Color.BLACK
                Color.BLACK shl 8 -> Color.WHITE
                else -> Color.WHITE
            }

            toColor = ColorUtils.blendARGB(fromColor, blendColor, 0.25f)
            textColor = swatch.bodyTextColor
        }

        // insert repository colors into database
        val repositoryColors = RepositoryColors(repositoryUuid, fromColor, toColor, textColor)
        repositoryColorsDao.insert(repositoryColors)

    }

    companion object {

        /**
         * Creates the [OneTimeWorkRequest] to be used for this Worker.
         *
         * @param uuidToImageUrl a list of Pair<String, String>. The first pair element is the Uuid of a repository.
         * The second pair element is the Url for the bitmap of that repository.
         */
        @Suppress("SpreadOperator")
        fun buildRequest(uuidToImageUrl: List<Pair<String, String>>): OneTimeWorkRequest {
            val inputData = workDataOf(*uuidToImageUrl.toTypedArray())

            return OneTimeWorkRequestBuilder<AvatarImageDownloadWorker>()
                .setInputData(inputData)
                .build()
        }
    }
}