package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant

/**
 * The WebHook of a repository.
 */
@Entity(tableName = "webHooks")
data class WebHook(
    @PrimaryKey
    val uuid: String,

    val workspaceUuid: String,
    val repositoryUuid: String,
    val firebaseToken: String,
    val events: List<WebHookEvent>,
    val createdAt: Instant
)