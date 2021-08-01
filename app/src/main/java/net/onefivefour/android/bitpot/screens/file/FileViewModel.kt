package net.onefivefour.android.bitpot.screens.file

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.SharedPrefsApp
import net.onefivefour.android.bitpot.data.repositories.SourcesRepository

/**
 * The ViewModel to load the content of a single file.
 * This ViewModel also allows to toggle between some display modes like "showing the line numbers"
 * or "wrap lines".
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class FileViewModel(private val sourcesRepository: SourcesRepository) : ViewModel() {

    private val isLineWrapEnabled = MutableLiveData<Boolean>().apply { postValue(SharedPrefsApp.fileViewerLineWrapEnabled) }
    private val areLineNumbersVisible = MutableLiveData<Boolean>().apply { postValue(SharedPrefsApp.fileViewerLineNumbersVisible) }

    /**
     * Observe this LiveData to receive the content of the given parameters
     */
    fun getFile(refHash: String, filePath: String, fileName: String): LiveData<String?> {
        return Transformations.map(sourcesRepository.getFile(refHash, filePath, fileName)) {
            it.data
        }
    }

    /**
     * Observe this LiveData to get updates on line wrapping
     */
    fun isLineWrapEnabled(): LiveData<Boolean> = isLineWrapEnabled

    /**
     * Observe this LiveData to get updates on the visibility of line numbers
     */
    fun areLineNumbersVisible(): LiveData<Boolean> = areLineNumbersVisible

    /**
     * Call this method to toggle line wrapping on and off.
     */
    fun toggleLineWrap() {
        val newSetting = !SharedPrefsApp.fileViewerLineWrapEnabled
        SharedPrefsApp.fileViewerLineWrapEnabled = newSetting
        isLineWrapEnabled.postValue(newSetting)
    }

    /**
     * call this method to toggle between showing and hiding the line numbers.
     */
    fun toggleLineNumbers() {
        val newSetting = !SharedPrefsApp.fileViewerLineNumbersVisible
        SharedPrefsApp.fileViewerLineNumbersVisible = newSetting
        areLineNumbersVisible.postValue(newSetting)
    }
}