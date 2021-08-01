package net.onefivefour.android.bitpot.network.apifields

/**
 * Extend this class to get the default api fields for paged responses
 */
abstract class PagedFields : ApiFieldsConfig() {

    fun getPagingFields() : Set<String> {
        return setOf("page", "totalItems", "nextPageUrl", "next")
    }

}