package net.onefivefour.android.bitpot.data.common

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.*
import net.onefivefour.android.bitpot.data.database.RepositoryColorsDao
import net.onefivefour.android.bitpot.data.model.RepositoryAvatar
import net.onefivefour.android.bitpot.data.model.RepositoryColors
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Giving a set of [RepositoryAvatar.Language] with their according repository Uuid, this Worker is responsible for
 * calculating the Color integers of all colors of this Repository Avatar and store it in the database.
 */
internal class LanguageRepositoryColorsWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val repositoryColorsDao: RepositoryColorsDao by inject()

    /**
     * the input data which is a list of repository uuid mapped to an integer array.
     * This integer array is created in the [buildRequest] method, since Worker inputData only
     * allows primitives.
     */
    private val repositoryUuidToIntArray = inputData.keyValueMap

    /**
     * First we have to revert the integer array of the input Data to a [RepositoryAvatar.Language] for
     * each repository. After that we are working through the given input data to calculate all colors and save them in the database.
     */
    override suspend fun doWork(): Result {
        if (repositoryUuidToIntArray.isEmpty()) return Result.failure()

        @Suppress("UNCHECKED_CAST")
        for ((repositoryUuid, intArray) in repositoryUuidToIntArray) {
            val array = intArray as Array<Int>
            val avatarLanguage = RepositoryAvatar.Language(array[0], array[1], array[2], array[3])
            calculateColors(avatarLanguage, repositoryUuid)
        }

        return Result.success()
    }

    /**
     * Use the given [RepositoryAvatar.Language] to store the Color values in the database.
     */
    private fun calculateColors(avatarLanguage: RepositoryAvatar.Language, repositoryUuid: String) {

        val fromColor = ContextCompat.getColor(applicationContext, avatarLanguage.gradientFromColor)
        val toColor = ContextCompat.getColor(applicationContext, avatarLanguage.gradientToColor)
        val textColor = ContextCompat.getColor(applicationContext, avatarLanguage.textColor)

        // insert repository colors into database
        val repositoryColors = RepositoryColors(repositoryUuid, fromColor, toColor, textColor)
        repositoryColorsDao.insert(repositoryColors)

    }

    companion object {

        /**
         * Creates the [OneTimeWorkRequest] to be used for this Worker.
         *
         * @param uuidToLanguage a list of Pair<String, String>. The first pair element is the Uuid of a repository.
         * The second pair element is the [RepositoryAvatar.Language] of that repository.
         */
        @Suppress("SpreadOperator")
        fun buildRequest(uuidToLanguage: List<Pair<String, RepositoryAvatar.Language>>): OneTimeWorkRequest {
            // workDataOf accepts only primitives and primitives-arrays.
            // Luckily we can easily create such an array from RepositoryAvatar.Language
            // We only have to make sure manually that we are referencing the right array element later on!!
            val convertedList = uuidToLanguage.map {
                val second = it.second
                it.first to arrayOf(second.drawable, second.gradientFromColor, second.gradientToColor, second.textColor)
            }

            val inputData = workDataOf(*convertedList.toTypedArray())

            return OneTimeWorkRequestBuilder<LanguageRepositoryColorsWorker>()
                .setInputData(inputData)
                .build()
        }
    }
}