package net.onefivefour.android.bitpot.data.common

import android.content.Context
import androidx.work.*
import net.onefivefour.android.bitpot.data.database.WebHooksDao
import net.onefivefour.android.bitpot.data.repositories.WebHooksRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.Executors

/**
 * This Worker deletes all web hooks passed in from the server and from the database.
 */
class DeleteWebHooksWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams), KoinComponent, Callback<Unit> {

    companion object {

        const val KEY_WEB_HOOKS_TO_DELETE = "KEY_WEB_HOOKS_TO_DELETE"

        /**
         * Creates the [OneTimeWorkRequest] to be used for this Worker.
         */
        fun buildRequest(uuidsToDelete: List<String>): OneTimeWorkRequest {
            val inputData = workDataOf(KEY_WEB_HOOKS_TO_DELETE to uuidsToDelete.toTypedArray())

            return OneTimeWorkRequestBuilder<DeleteWebHooksWorker>()
                .setInputData(inputData)
                .build()
        }
    }

    private val webHooksDao: WebHooksDao by inject()

    private val webHooksRepository: WebHooksRepository by inject()

    private val uuidsToDelete = inputData.getStringArray(KEY_WEB_HOOKS_TO_DELETE)


    override suspend fun doWork(): Result {
        Timber.d("+++ DeleteWebHooksWorker.doWork() started")

        if (uuidsToDelete.isNullOrEmpty()) {
            Timber.e("+++ uuidsToDelete are empty or null -> nothing to delete")
            return Result.success()
        }

        uuidsToDelete.forEach {
            webHooksRepository.deleteWebHook(it, this)
        }

        return Result.success()
    }

    /**
     * Called when the web hook could not be deleted from the server
     */
    override fun onFailure(call: Call<Unit>, t: Throwable) {
        Timber.d("+++ Web Hook was not deleted from server -> also delete from DB")
        Executors.newSingleThreadExecutor().execute {
            val uuid = call.request().url.encodedPathSegments.last()
            webHooksDao.delete(uuid)
        }
    }

    /**
     * Called when the web hook was successfully deleted from the server
     */
    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
        Timber.d("+++ Web Hook was deleted from server -> also delete from DB")
        Executors.newSingleThreadExecutor().execute {
            val uuid = call.request().url.encodedPathSegments.last()
            webHooksDao.delete(uuid)
        }
    }
}