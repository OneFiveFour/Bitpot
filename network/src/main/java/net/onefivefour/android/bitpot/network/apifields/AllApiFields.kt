package net.onefivefour.android.bitpot.network.apifields

/**
 * The fallback config to set the requested Api fields.
 * returns am empty set, so that all fields are returned by the API.
 */
class AllApiFields : ApiFieldsConfig() {
    override fun getFields(): Set<String> {
        return emptySet()
    }
}