package net.onefivefour.android.bitpot.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.RefSelection

/**
 * An interface describing all possible database operations for the [RefSelection] table
 */
@Dao
interface RefSelectionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(refs : RefSelection)

    @Query("SELECT * FROM refSelections WHERE repoUuid = :repoUuid LIMIT 1")
    suspend fun getByRepoUuid(repoUuid: String): RefSelection?

}
