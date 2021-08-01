package net.onefivefour.android.bitpot.data.model.converter

import android.net.Uri
import net.onefivefour.android.bitpot.data.common.DataNetworkConverter
import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.common.WebHookData
import net.onefivefour.android.bitpot.data.extensions.toInstant
import net.onefivefour.android.bitpot.data.model.WebHookEvent
import net.onefivefour.android.bitpot.data.model.WebHook as AppWebHook
import net.onefivefour.android.bitpot.network.model.webhooks.PostWebHook as NetworkPostWebHook
import net.onefivefour.android.bitpot.network.model.webhooks.WebHook as NetworkWebHook

/**
 * Converts a [NetworkWebHook] into a app domain [AppWebHook].
 * Also converts an [AppWebHook] into a [NetworkPostWebHook] to send it in a POST or PUT request.
 */
class WebHookConverter : NetworkDataConverter<NetworkWebHook, AppWebHook>, DataNetworkConverter<AppWebHook, NetworkPostWebHook> {

    override fun toAppModel(item: NetworkWebHook): AppWebHook {

        val uuid = item.uuid
        val workspaceUuid = Uri.parse(item.url).getQueryParameter("wsId") ?: ""
        val repositoryUuid = item.subject.uuid
        val firebaseToken = Uri.parse(item.url).getQueryParameter("pushToken") ?: ""
        val events = item.events.map { WebHookEventConverter.toAppModel(it) }.distinct()
        val createdAt = item.createdAt.toInstant()

        return AppWebHook(uuid, workspaceUuid, repositoryUuid, firebaseToken, events, createdAt)
    }

    override fun toNetworkModel(from: AppWebHook): NetworkPostWebHook {

        val active = true
        val description = WebHookData.getDescription()
        val events = from.events.map { WebHookEventConverter.toNetworkModel(it) }.flatten().distinct()
        val url = WebHookData.getUrl()

        return NetworkPostWebHook(active, description, events, url)
    }

    fun toNetworkModel(from: List<WebHookEvent>): NetworkPostWebHook {

        val active = true
        val description = WebHookData.getDescription()
        val events = from.map { WebHookEventConverter.toNetworkModel(it) }.flatten().distinct()
        val url = WebHookData.getUrl()

        return NetworkPostWebHook(active, description, events, url)
    }

}
