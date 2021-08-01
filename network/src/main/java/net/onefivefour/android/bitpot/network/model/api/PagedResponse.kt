package net.onefivefour.android.bitpot.network.model.api

/**
 * Interface for all paged responses coming from the Bitbucket API
 *
 * @property page is the page number of the response
 * @property values is the list of payload objects of the response
 * @property totalItems is the number of total items in the whole list
 * @property nextPageUrl is the API URL to the next page
 */
interface PagedResponse<T> {
    val page: Int
    val values: List<T>
    val totalItems: Int
    val nextPageUrl: String?
}