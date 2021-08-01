package net.onefivefour.android.bitpot.data.repositories

import net.onefivefour.android.bitpot.data.database.RepositoryColorsDao
import org.koin.core.KoinComponent

/**
 * The 'Repository' classes should always be the only api contact for the ui layer.
 * The ViewModels can get all their data from here and should not call any other classes directly.
 * 
 * This repository serves information about the colors of a Bitbucket repository.
 * 
 */
class RepoColorsRepository(private val repositoryColorsDao: RepositoryColorsDao) : KoinComponent {

    fun getRepositoryColors(repositoryUuid: String) = repositoryColorsDao.getByRepositoryUuid(repositoryUuid)    

}