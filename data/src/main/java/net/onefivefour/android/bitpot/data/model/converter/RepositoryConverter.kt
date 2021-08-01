package net.onefivefour.android.bitpot.data.model.converter

import android.net.Uri
import androidx.work.WorkManager
import net.onefivefour.android.bitpot.data.R
import net.onefivefour.android.bitpot.data.common.AvatarImageDownloadWorker
import net.onefivefour.android.bitpot.data.common.LanguageRepositoryColorsWorker
import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.extensions.toInstant
import net.onefivefour.android.bitpot.data.model.PipelineState
import net.onefivefour.android.bitpot.data.model.Repository
import net.onefivefour.android.bitpot.data.model.RepositoryAvatar
import org.koin.core.KoinComponent
import org.koin.core.inject
import net.onefivefour.android.bitpot.network.model.repositories.Repository as NetworkRepository

/**
 * Converts a [NetworkRepository] into a app domain [Repository].
 */
class RepositoryConverter : NetworkDataConverter<NetworkRepository, Repository>, KoinComponent {

    private val repoImageWorkManager: WorkManager by inject()

    private val repoLanguageWorkManager: WorkManager by inject()

    override fun toAppModel(item: NetworkRepository): Repository {

        // try converting last pipeline
        val pipeline = when (val networkPipeline = item.lastPipeline) {
            null -> null
            else -> PipelineConverter().toAppModel(networkPipeline)
        }

        val avatar = getRepoAvatar(item.links.avatar.href)

        return Repository(
            item.uuid,
            item.name,
            item.workspace.uuid,
            avatar,
            item.updatedOn.toInstant(),
            item.isPrivate,
            pipeline?.state ?: PipelineState.UNKNOWN,
            item.mainBranch?.name ?: "master"
        )
    }

    /**
     * starts background workers to create the correct avatar images
     * and repository colors for the given list of repositories
     */
    fun computeRepositoryAvatars(repositories: List<Repository>) {
        // compute repos with avatar-images to download
        val imageWorkerInput = repositories
            .filter { it.avatar is RepositoryAvatar.Image }
            .map { it.uuid to (it.avatar as RepositoryAvatar.Image).url }
        val imageWorkerRequest = AvatarImageDownloadWorker.buildRequest(imageWorkerInput)
        repoImageWorkManager.enqueue(imageWorkerRequest)

        // compute repos without avatar images
        val languageWorkerInput = repositories
            .filter { it.avatar is RepositoryAvatar.Language }
            .map { it.uuid to it.avatar as RepositoryAvatar.Language }
        val languageWorkerRequest = LanguageRepositoryColorsWorker.buildRequest(languageWorkerInput)
        repoLanguageWorkManager.enqueue(languageWorkerRequest)
    }

    /**
     * Tries to extract a default language drawable from the avatarUrl.
     * If no matching language is found in [repositoryLanguages], the given avatarUrl
     * will be returned instead. This URL can then be used to load the repo avatar.
     */
    private fun getRepoAvatar(avatarUrl: String): RepositoryAvatar {
        val url = Uri.parse(avatarUrl)
        val avatarValue = url.getQueryParameter("ts").toString()
        if (repositoryLanguages.containsKey(avatarValue)) {
            return repositoryLanguages[avatarValue] ?: RepositoryAvatar.Image(avatarUrl)
        }
        return RepositoryAvatar.Image(avatarUrl)
    }

    /**
     * a map from all known repo languages to their drawables
     */
    @Suppress("MaxLineLength")
    private val repositoryLanguages = mapOf(
        "actionscript"  to RepositoryAvatar.Language(R.drawable.repo_avatar_action_script, R.color.repo_action_script_gradient_from, R.color.repo_action_script_gradient_to, R.color.repo_text_action_script),
        "android"       to RepositoryAvatar.Language(R.drawable.repo_avatar_android, R.color.repo_android_gradient_from, R.color.repo_android_gradient_to, R.color.repo_text_android),
        "c"             to RepositoryAvatar.Language(R.drawable.repo_avatar_c, R.color.repo_c_gradient_from, R.color.repo_c_gradient_to, R.color.repo_text_c),
        "c_plus_plus"   to RepositoryAvatar.Language(R.drawable.repo_avatar_c_plus_plus, R.color.repo_c_plus_plus_gradient_from, R.color.repo_c_plus_plus_gradient_to, R.color.repo_text_c_plus_plus),
        "c_sharp"       to RepositoryAvatar.Language(R.drawable.repo_avatar_c_sharp, R.color.repo_c_sharp_gradient_from, R.color.repo_c_sharp_gradient_to, R.color.repo_text_c_sharp),
        "coldfusion"    to RepositoryAvatar.Language(R.drawable.repo_avatar_cold_fusion, R.color.repo_cold_fusion_gradient_from, R.color.repo_cold_fusion_gradient_to, R.color.repo_text_cold_fusion),
        "default"       to RepositoryAvatar.Language(R.drawable.repo_avatar_default, R.color.repo_default_gradient_from, R.color.repo_default_gradient_to, R.color.repo_text_default),
        "dart"          to RepositoryAvatar.Language(R.drawable.repo_avatar_dart, R.color.repo_dart_gradient_from, R.color.repo_dart_gradient_to, R.color.repo_text_dart),
        "go"            to RepositoryAvatar.Language(R.drawable.repo_avatar_go, R.color.repo_go_gradient_from, R.color.repo_go_gradient_to, R.color.repo_text_go),
        "haskell"       to RepositoryAvatar.Language(R.drawable.repo_avatar_haskell, R.color.repo_haskell_gradient_from, R.color.repo_haskell_gradient_to, R.color.repo_text_haskell),
        "html5"         to RepositoryAvatar.Language(R.drawable.repo_avatar_html, R.color.repo_html_gradient_from, R.color.repo_html_gradient_to, R.color.repo_text_html),
        "java"          to RepositoryAvatar.Language(R.drawable.repo_avatar_java, R.color.repo_java_gradient_from, R.color.repo_java_gradient_to, R.color.repo_text_java),
        "js"            to RepositoryAvatar.Language(R.drawable.repo_avatar_java_script, R.color.repo_java_script_gradient_from, R.color.repo_java_script_gradient_to, R.color.repo_text_java_script),
        "latex"         to RepositoryAvatar.Language(R.drawable.repo_avatar_latex, R.color.repo_latex_gradient_from, R.color.repo_latex_gradient_to, R.color.repo_text_latex),
        "markdown"      to RepositoryAvatar.Language(R.drawable.repo_avatar_markdown, R.color.repo_markdown_gradient_from, R.color.repo_markdown_gradient_to, R.color.repo_text_markdown),
        "nodejs"        to RepositoryAvatar.Language(R.drawable.repo_avatar_node_js, R.color.repo_node_js_gradient_from, R.color.repo_node_js_gradient_to, R.color.repo_text_node_js),
        "obj_c"         to RepositoryAvatar.Language(R.drawable.repo_avatar_objective_c, R.color.repo_objective_c_gradient_from, R.color.repo_objective_c_gradient_to, R.color.repo_text_objective_c),
        "php"           to RepositoryAvatar.Language(R.drawable.repo_avatar_php, R.color.repo_php_gradient_from, R.color.repo_php_gradient_to, R.color.repo_text_php),
        "python"        to RepositoryAvatar.Language(R.drawable.repo_avatar_python, R.color.repo_python_gradient_from, R.color.repo_python_gradient_to, R.color.repo_text_python),
        "ruby"          to RepositoryAvatar.Language(R.drawable.repo_avatar_ruby, R.color.repo_ruby_gradient_from, R.color.repo_ruby_gradient_to, R.color.repo_text_ruby),
        "scala"         to RepositoryAvatar.Language(R.drawable.repo_avatar_scala, R.color.repo_scala_gradient_from, R.color.repo_scala_gradient_to, R.color.repo_text_scala),
        "dotnet"        to RepositoryAvatar.Language(R.drawable.repo_avatar_vb_net, R.color.repo_vb_net_gradient_from, R.color.repo_vb_net_gradient_to, R.color.repo_text_vb_net)
    )
}