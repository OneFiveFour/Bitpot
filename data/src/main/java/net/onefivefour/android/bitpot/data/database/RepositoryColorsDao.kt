package net.onefivefour.android.bitpot.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.RepositoryColors

/**
 * An interface describing all possible database operations for the [RepositoryColors] table
 */
@Dao
interface RepositoryColorsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repositories : RepositoryColors)

    @Query("SELECT * FROM repositoryColors WHERE uuid = :uuid LIMIT 1")
    fun getByRepositoryUuid(uuid: String): LiveData<RepositoryColors>

    @Query("DELETE FROM repositoryColors")
    fun delete()
}
