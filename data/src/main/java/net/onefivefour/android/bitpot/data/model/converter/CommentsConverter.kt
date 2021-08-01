package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.model.Comment
import net.onefivefour.android.bitpot.network.model.comments.Comments as NetworkComments

/**
 * Converts a [NetworkComments] into a list of app domain [Comment]s.
 */
class CommentsConverter : NetworkDataConverter<NetworkComments, List<Comment>> {
    
    override fun toAppModel(item: NetworkComments): List<Comment> {
        return item.values.map { CommentConverter().toAppModel(it) }
    }

}
