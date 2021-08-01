package net.onefivefour.android.bitpot.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.Workspace

/**
 * An interface describing all possible database operations for the [Workspace] table
 */
@Dao
interface WorkspacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workspaces : List<Workspace>)

    @Query("SELECT * FROM workspaces ORDER BY displayName")
    fun get(): LiveData<List<Workspace>>

    @Query("DELETE FROM workspaces")
    fun delete()

}
