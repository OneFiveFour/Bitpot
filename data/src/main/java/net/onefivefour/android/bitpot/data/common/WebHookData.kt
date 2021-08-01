package net.onefivefour.android.bitpot.data.common

import net.onefivefour.android.bitpot.data.BitpotData

/**
 * Simple helper object for fixed data related to WebHooks.
 */
object WebHookData {

    /**
     * The returned Url includes the following GET parameter:
     *  * pushToken: This is the current firebase token to identify this device
     *  * acctId: This is the current users accountId. It is used by the [net.onefivefour.android.bitpot.data.model.converter.WebHooksSyncingConverter] to make sure the right webhook is synced 
     *  (one repository might have several webhooks from bitpot)
     *  * wsId: This is the current workspace Uuid. This uuid is passed on in the server script to create the local push notifications. As soon as this Bitbucket Bug is fixed, this parameter can be
     *  removed and instead fetched from the web hook payload: https://jira.atlassian.com/browse/BCLOUD-20239
     * 
     * @return an Url for a web hook that describes the current account and workspace
     */
    fun getUrl() = "https://bitpot-app.com/push/v1/fcm.php?pushToken=${BitpotData.getFirebaseToken()}&accId=${BitpotData.getAccountId()}&wsId=${BitpotData.getSelectedWorkspaceUuid()}"

    fun getDescription() = "Bitpot (AccountId: ${BitpotData.getAccountId()})"

}