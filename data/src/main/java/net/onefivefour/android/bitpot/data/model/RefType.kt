package net.onefivefour.android.bitpot.data.model

import net.onefivefour.android.bitpot.network.model.refs.Ref

/**
 * This enum lists all possible [Ref]s of a repository.
 */
enum class RefType {
    BRANCH, TAG, ALL
}