package net.onefivefour.android.bitpot.network.apifields

/**
 * Implement this for each Api Call to only receive certain fields
 * instead of the full api response.
 *
 * See for details:
 * https://developer.atlassian.com/bitbucket/api/2/reference/meta/partial-response
 * https://developer.atlassian.com/bitbucket/api/2/reference/meta/filtering
 */
abstract class ApiFieldsConfig {

    fun get() =  getFields().joinToString(separator = ",")

    abstract fun getFields(): Set<String>
}