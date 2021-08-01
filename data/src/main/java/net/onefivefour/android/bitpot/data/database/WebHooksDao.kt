package net.onefivefour.android.bitpot.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.WebHook

/**
 * An interface describing all possible database operations for the [WebHook]s table
 */
@Dao
interface WebHooksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(webHook : WebHook)

    @Query("SELECT * FROM webHooks WHERE uuid = :uuid")
    fun get(uuid: String): WebHook

    @Query("SELECT * FROM webHooks WHERE workspaceUuid = :workspaceUuid AND repositoryUuid = :repositoryUuid LIMIT 1")
    fun get(workspaceUuid: String, repositoryUuid: String): LiveData<WebHook>

    @Query("DELETE FROM webHooks WHERE uuid = :uuid")
    fun delete(uuid: String)

    @Query("DELETE FROM webHooks WHERE workspaceUuid = :workspaceUuid AND repositoryUuid = :repositoryUuid")
    fun delete(workspaceUuid: String, repositoryUuid: String)

    @Query("SELECT * FROM webHooks")
    fun getAll(): List<WebHook>

    @Query("SELECT * FROM webHooks")
    fun getAllAsLiveData(): LiveData<List<WebHook>>

    @Query("SELECT uuid FROM webHooks WHERE firebaseToken != :firebaseToken")
    fun getAllExcept(firebaseToken: String) : List<String>
}
