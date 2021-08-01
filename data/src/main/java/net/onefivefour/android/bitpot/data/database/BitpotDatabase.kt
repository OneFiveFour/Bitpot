package net.onefivefour.android.bitpot.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.onefivefour.android.bitpot.data.database.converter.*
import net.onefivefour.android.bitpot.data.database.pagingkeys.*
import net.onefivefour.android.bitpot.data.model.*
import net.onefivefour.android.bitpot.data.model.paging.*


/**
 * The main database of this app.
 * It holds data for offline access and especially to serve as [androidx.paging.DataSource] for paged data.
 * For more details have a look at the Android paging library or the 'net.onefivefour.android.bitpot.network.paging'
 * package in the network module of this app.
 */
@Database(
    entities = [
        Comment::class,
        Download::class,
        Pipeline::class,
        PullRequest::class,
        Ref::class,
        RefSelection::class,
        Repository::class,
        RepositoryColors::class,
        Source::class,
        SourcePagingKey::class,
        PullRequestPagingKey::class,
        CommentPagingKey::class,
        PipelinePagingKey::class,
        DownloadPagingKey::class,
        RefPagingKey::class,
        RepoPagingKey::class,
        WebHook::class,
        Workspace::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    CommentAuthorConverter::class,
    CommentPositionConverter::class,
    InstantConverter::class,
    PipelineStateConverter::class,
    PipelineTargetConverter::class,
    PullRequestStateConverter::class,
    RefConverter::class,
    RefsTypeConverter::class,
    RepositoryAvatarConverter::class,
    SourceTypeConverter::class,
    WebHookEventsConverter::class
)
abstract class BitpotDatabase : RoomDatabase() {

    /**
     * get a handle on the [RepositoriesDao]
     */
    abstract fun repositories(): RepositoriesDao

    /**
     * get a handle on the [RepositoryColorsDao]
     */
    abstract fun repositoryColors(): RepositoryColorsDao

    /**
     * get a handle on the [PipelinesDao]
     */
    abstract fun pipelines(): PipelinesDao

    /**
     * get a handle on the [PullRequestsDao]
     */
    abstract fun pullRequests(): PullRequestsDao

    /**
     * get a handle on the [DownloadsDao]
     */
    abstract fun downloads(): DownloadsDao

    /**
     * get a handle on the [SourcesDao]
     */
    abstract fun sources(): SourcesDao

    /**
     * get a handle on the [SourcePagingKeysDao]
     */
    abstract fun sourcePagingKeys(): SourcePagingKeysDao

    /**
     * get a handle on the [PullRequestPagingKeysDao]
     */
    abstract fun pullRequestPagingKeys(): PullRequestPagingKeysDao

    /**
     * get a handle on the [PipelinePagingKeysDao]
     */
    abstract fun pipelinesPagingKeys(): PipelinePagingKeysDao

    /**
     * get a handle on the [CommentPagingKeysDao]
     */
    abstract fun commentsPagingKeys(): CommentPagingKeysDao

    /**
     * get a handle on the [RefPagingKeysDao]
     */
    abstract fun refPagingKeys(): RefPagingKeysDao

    /**
     * get a handle on the [DownloadPagingKeysDao]
     */
    abstract fun downloadPagingKeys(): DownloadPagingKeysDao

    /**
     * get a handle on the [RefPagingKeysDao]
     */
    abstract fun repoPagingKeys(): RepoPagingKeysDao

    /**
     * get a handle on the [RefsDao]
     */
    abstract fun refs(): RefsDao

    /**
     * get a handle on the [RefSelectionsDao]
     */
    abstract fun refSelection(): RefSelectionsDao

    /**
     * get a handle on the [CommentsDao]
     */
    abstract fun comments(): CommentsDao

    /**
     * get a handle on the [WebHooksDao]
     */
    abstract fun webHooks(): WebHooksDao

    /**
     * get a handle on the [WebHooksDao]
     */
    abstract fun workspaces(): WorkspacesDao
}