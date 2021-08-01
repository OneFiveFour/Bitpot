package net.onefivefour.android.bitpot.screens.sources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import net.onefivefour.android.bitpot.data.database.SourcesDao
import net.onefivefour.android.bitpot.data.model.*
import net.onefivefour.android.bitpot.data.repositories.RefsRepository
import net.onefivefour.android.bitpot.data.repositories.mediators.SourcesRemoteMediator
import java.util.*

/**
 * This ViewModel is responsible for loading the list of [Source]s whenever a
 * repository is selected. This list is coming from the database.
 *
 * To fetch the right sources, we need the following information:
 *
 * * the selected [Repository] (mandatory)
 * * a valid [Ref] of this repository (mandatory)
 * * a valid source path (optional, default = root path)
 *
 * When opening the [SourcesFragment], a repository was selected and passed
 * into this ViewModel via [setRepository]. For this repository we try to find the 
 * best branch or tag using and then load all sources from the root directory of this 
 * branch or tag.
 * 
 * You can always call [setSelectedRef] or [setSelectedPath] to change the branch/tag or 
 * the current directory.
 */
@Suppress("EXPERIMENTAL_API_USAGE")
@ExperimentalPagingApi
class SourcesViewModel(private val sourcesDao: SourcesDao, private val refsRepository: RefsRepository) : ViewModel() {

    /**
     * A description of the current source list. It contains the repositoryUuid, the [Ref] and the 
     * current file path.
     */
    private val sourceDescription = MutableStateFlow(SourceDescription())

    /**
     * Observe this LiveData to get the currently selected directory path
     */
    val selectedPath = sourceDescription.mapLatest { sourceDescription ->
        sourceDescription.path
    }.asLiveData()

    /**
     * Observe this LiveData to get the currently selected branch or tag
     */
    val selectedRefName = sourceDescription.mapLatest { sourceDescription ->
        sourceDescription.refName
    }.asLiveData()

    /**
     * Collect this Flow to get the list of files of the current directory
     * of the current branch/tag.
     */
    fun getSources() = sourceDescription.flatMapLatest { sourceDescription ->

        val repositoryUuid = sourceDescription.repositoryUuid
        val refName = sourceDescription.refName
        val path = sourceDescription.path

        Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            remoteMediator = SourcesRemoteMediator(repositoryUuid, refName, path), 
            pagingSourceFactory = { sourcesDao.get(repositoryUuid, refName, path) }
        )
            .flow
            .map { pagingData ->
                // add "folder up navigation" for all non-root directories
                val isRootDirectory = path.isEmpty()
                if (!isRootDirectory) {                    
                    return@map addFolderUpItem(repositoryUuid, refName, path, pagingData)
                }
                pagingData
            }
            .cachedIn(viewModelScope)
    }

    /**
     * Adds a "fake" Source for all non-root directories to navigate up to the parent directory.
     */
    private fun addFolderUpItem(repositoryUuid: String, refName: String, path: String, pagingData: PagingData<Source>): PagingData<Source> {
        val folderUpSource = Source(
            UUID.randomUUID().toString(),
            repositoryUuid,
            refName,
            path,
            "..",
            SourceType.FOLDER_UP,
            0L
        )
        return pagingData.insertHeaderItem(TerminalSeparatorType.SOURCE_COMPLETE, folderUpSource)
    }

    /**
     * sets the current Bitbucket repository to this ViewModel.
     * This will trigger loading all sources from the last visited branch/tag or 
     * from the main branch of the given repository.
     */
    fun setRepository(repository: Repository) {
        viewModelScope.launch {

            val refName = computeSelectedRef(repository.mainBranch)

            // update the sourceDescription with the new data
            val newSourceDescription = SourceDescription(
                repository.uuid,
                refName,
                ""
            )
            emitSourceDescription(newSourceDescription)
        }
    }

    /**
     * Update the branch/tag or the current repository.
     * This will trigger loading the sources of the root directory of 
     * the given [Ref]
     */
    fun setSelectedRef(ref: Ref) {
        viewModelScope.launch {
            val repoUuid = getRepoUuid()
            val refName = ref.name

            // Whenever we switch to another Ref, we go back to the root directory for now.
            val newSourceDescription = SourceDescription(repoUuid, refName, "")
            emitSourceDescription(newSourceDescription)
        }
    }

    /**
     * Update the file path or the current repository.
     * This will navigate to the given path and load all sources
     * in this path.
     */
    fun setSelectedPath(path: String) {
        viewModelScope.launch {
            val repositoryUuid = getRepoUuid()
            val refName = sourceDescription.value.refName
            val newSourceDescription = SourceDescription(repositoryUuid, refName, path)
            emitSourceDescription(newSourceDescription)
        }
    }

    private suspend fun emitSourceDescription(newSourceDescription: SourceDescription) {
        sourceDescription.emit(newSourceDescription)
    }

    /**
     * @return if we already stored a selected Ref for the current repository returns the refName of this selection.
     * Otherwise returns the given main branch
     */
    private suspend fun computeSelectedRef(mainBranch: String): String {
        val repoUuid = getRepoUuid()
        val savedRefSelection = refsRepository.getRefSelection(repoUuid) ?: return mainBranch
        return savedRefSelection.ref.name
    }

    private fun getRepoUuid(): String {
        return sourceDescription.value.repositoryUuid
    }

    /**
     * Call this method to navigate to the parent folder
     * of the current file path.
     */
    fun navigateUp() {
        val currentPath = sourceDescription.value.path
        val lastPathSegmentIndex = currentPath.lastIndexOf("/")
        val navigateToRoot = lastPathSegmentIndex == -1
        val newPath = when {
            navigateToRoot -> ""
            else -> currentPath.substring(0, lastPathSegmentIndex)
        }
        setSelectedPath(newPath)
    }
}
