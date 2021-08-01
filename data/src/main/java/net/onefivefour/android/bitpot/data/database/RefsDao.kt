package net.onefivefour.android.bitpot.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.onefivefour.android.bitpot.data.model.Ref
import net.onefivefour.android.bitpot.data.model.RefType

/**
 * An interface describing all possible database operations for the [Ref] table
 */
@Dao
interface RefsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(refs : List<Ref>)

    @Query("SELECT * FROM refs WHERE id = :refId")
    fun getById(refId: String) : Ref

    @Query("SELECT * FROM refs WHERE repoUuid = :repoUuid")
    fun getByRepoUuid(repoUuid: String): PagingSource<Int, Ref>

    @Query("SELECT * FROM refs WHERE repoUuid = :repoUuid AND type = :refType")
    fun getByRepoUuid(repoUuid: String, refType: RefType): PagingSource<Int, Ref>
    
    @Query("DELETE FROM refs WHERE repoUuid = :repoUuid")
    suspend fun delete(repoUuid: String)

    @Query("SELECT * FROM refs WHERE repoUuid = :repoUuid")
    fun getAll(repoUuid: String): List<Ref>

}
