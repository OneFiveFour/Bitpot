package net.onefivefour.android.bitpot.network.retrofit

import androidx.lifecycle.LiveData
import net.onefivefour.android.bitpot.network.common.ApiResponse
import net.onefivefour.android.bitpot.network.model.approve.Participant
import net.onefivefour.android.bitpot.network.model.comments.Comment
import net.onefivefour.android.bitpot.network.model.comments.Comments
import net.onefivefour.android.bitpot.network.model.comments.PostComment
import net.onefivefour.android.bitpot.network.model.downloads.Downloads
import net.onefivefour.android.bitpot.network.model.merge.PostMerge
import net.onefivefour.android.bitpot.network.model.pipelines.Pipelines
import net.onefivefour.android.bitpot.network.model.pullrequest.PullRequest
import net.onefivefour.android.bitpot.network.model.pullrequests.PullRequests
import net.onefivefour.android.bitpot.network.model.refs.Refs
import net.onefivefour.android.bitpot.network.model.repositories.Repositories
import net.onefivefour.android.bitpot.network.model.repositories.Repository
import net.onefivefour.android.bitpot.network.model.sources.Sources
import net.onefivefour.android.bitpot.network.model.user.User
import net.onefivefour.android.bitpot.network.model.webhooks.PostWebHook
import net.onefivefour.android.bitpot.network.model.webhooks.WebHook
import net.onefivefour.android.bitpot.network.model.webhooks.WebHooks
import net.onefivefour.android.bitpot.network.model.workspace.Workspaces
import retrofit2.Call
import retrofit2.http.*

/**
 * The Retrofit API definition for calls toward the Bitbucket Api.
 */
interface BitbucketApi {

    companion object {
        const val BASE_URL = "https://api.bitbucket.org/2.0/"
    }

    @GET("user")
    fun getUser(): LiveData<ApiResponse<User>>

    @GET("workspaces?pagelen=100")
    fun getWorkspaces() : LiveData<ApiResponse<Workspaces>>

    @GET("repositories/{workspaceUuid}?sort=-updated_on")
    suspend fun getRepositories(@Path("workspaceUuid") workspaceUuid: String, @Query("page") page: Int?, @Query("pagelen") pageLength: Int): Repositories

    @GET("repositories/{workspaceUuid}/{repositoryUuid}")
    fun getRepository(@Path("workspaceUuid") workspaceUuid: String, @Path("repositoryUuid") repositoryUuid: String): LiveData<ApiResponse<Repository>>

    @GET("repositories/{repositoryFullName}/pipelines/?sort=-created_on")
    suspend fun getPipelines(
        @Query("page") pageId: Int?,
        @Query("pagelen") pageLength: Int
    ): Pipelines

    @GET("repositories/{repositoryFullName}/pullrequests/?sort=-created_on")
    suspend fun getPullRequests(
        @Query("page") pageId: Int?, 
        @Query("pagelen") pageLength: Int
    ): PullRequests

    @GET("repositories/{repositoryFullName}/pullrequests/{pullRequestId}")
    fun getPullRequest(@Path("pullRequestId") pullRequestId: Int): LiveData<ApiResponse<PullRequest>>

    @GET("repositories/{repositoryFullName}/pullrequests/{pullRequestId}/comments?q=deleted=false&pagelen=100")
    fun getComments(
        @Path("pullRequestId") pullRequestId: Int,
        @Query("page") pageId: Int? = 1
    ): LiveData<ApiResponse<Comments>>

    @POST("repositories/{repositoryFullName}/pullrequests/{pullRequestId}/comments")
    fun postComment(@Path("pullRequestId") pullRequestId: Int, @Body comment: PostComment): LiveData<ApiResponse<Comment>>

    @DELETE("repositories/{repositoryFullName}/pullrequests/{pullRequestId}/comments/{commentId}")
    fun deleteComment(@Path("pullRequestId") pullRequestId: Int, @Path("commentId") commentId: Int): LiveData<ApiResponse<Unit>>

    @POST("repositories/{repositoryFullName}/pullrequests/{pullRequestId}/approve")
    fun postApproval(@Path("pullRequestId") pullRequestId: Int, @Body body: String = ""): LiveData<ApiResponse<Participant>>

    @DELETE("repositories/{repositoryFullName}/pullrequests/{pullRequestId}/approve")
    fun deleteApproval(@Path("pullRequestId") pullRequestId: Int): LiveData<ApiResponse<Unit>>

    @POST("repositories/{repositoryFullName}/pullrequests/{pullRequestId}/merge")
    fun postMerge(@Path("pullRequestId") pullRequestId: Int, @Body body: PostMerge): LiveData<ApiResponse<PullRequest>>

    @GET("repositories/{repositoryFullName}/pullrequests/{pullRequestId}/diff")
    fun getDiff(@Path("pullRequestId") pullRequestId: Int): LiveData<ApiResponse<String>>

    @GET("repositories/{repositoryFullName}/downloads?sort=-created_on")
    suspend fun getDownloads(
        @Query("page") pageId: Int?,
        @Query("pagelen") pageLength: Int
    ): Downloads

    @GET("repositories/{repositoryFullName}/refs{refTypeName}")
    suspend fun getRefs(
        @Path("refTypeName") refTypeName: String,
        @Query("page") pageId: String?,
        @Query("pagelen") pageLength: Int
    ): Refs

    @GET("repositories/{repositoryFullName}/src/{refName}/{path}")
    suspend fun getSources(
        @Path("refName") refName: String,
        @Path("path") path: String,
        @Query("page") pageId: String?,
        @Query("pagelen") pageLength: Int
    ): Sources

    @GET("repositories/{repositoryFullName}/src/{refName}/{filePath}/{fileName}")
    fun getFile(
        @Path("refName") refName: String,
        @Path("filePath") path: String,
        @Path("fileName") fileName: String
    ): LiveData<ApiResponse<String>>

    @GET("repositories/{workspaceUuid}/{repositoryUuid}/hooks?page=1&pagelen=100")
    fun getWebHooks(
        @Path("workspaceUuid") workspaceUuid: String,
        @Path("repositoryUuid") repositoryUuid: String
    ): LiveData<ApiResponse<WebHooks>>

    @POST("repositories/{repositoryFullName}/hooks")
    fun postWebHook(@Body webHook: PostWebHook): LiveData<ApiResponse<WebHook>>

    @PUT("repositories/{repositoryFullName}/hooks/{webHookUuid}")
    fun updateWebHook(@Body webHook: PostWebHook, @Path("webHookUuid") webHookUuid: String): LiveData<ApiResponse<WebHook>>

    @DELETE("repositories/{repositoryFullName}/hooks/{webHookUuid}")
    fun deleteWebHook(@Path("webHookUuid") webHookUuid: String): LiveData<ApiResponse<Unit>>

    @DELETE("repositories/{repositoryFullName}/hooks/{webHookUuid}")
    fun deleteWebHookSilent(@Path("webHookUuid") webHookUuid: String): Call<Unit>

    @PUT("repositories/{workspaceUuid}/{repositoryUuid}/hooks/{webHookUuid}")
    fun updateFirebaseToken(
        @Path("workspaceUuid") workspaceUuid: String,
        @Path("repositoryUuid") repositoryUuid: String,
        @Path("webHookUuid") webHookUuid: String,
        @Body webHook: PostWebHook
    ): Call<WebHook>

}