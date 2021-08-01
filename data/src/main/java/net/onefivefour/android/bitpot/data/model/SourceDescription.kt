package net.onefivefour.android.bitpot.data.model


/**
 * This is a wrapper class holding all parameters to identify
 * a specific source file location in a repository. 
 */
data class SourceDescription(
    val repositoryUuid: String = "",
    val refName: String = "",
    val path: String = ""
)
