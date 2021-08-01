package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import java.io.Serializable

/**
 * The data class representing a Repository.
 * This is also the description of the database table for repositories.
 *
 * This class implements Serializable to allow it to be part of a
 * navigation argument. That is needed to enable a smooth transition animation
 * between PullRequestsFragment and PullRequestActivity.
 */
@Entity(
    tableName = "pullRequests"
)
data class PullRequest(

    /**
     * Pull Request do not have a uuid on their own.
     * But to store them in Room, we need a globally unique field as PrimaryKey.
     * The url to the pull request fulfills this requirement.
     */
    @PrimaryKey
    val selfUrl: String,

    val id: Int,

    val repoUuid: String,
    val sourceBranchName: String,
    val destinationBranchName: String,
    val lastUpdated: Instant,
    val state: PullRequestState,
    val creatorAvatarUrl: String
) : Serializable