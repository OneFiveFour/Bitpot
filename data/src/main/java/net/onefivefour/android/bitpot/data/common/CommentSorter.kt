package net.onefivefour.android.bitpot.data.common

import net.onefivefour.android.bitpot.data.model.Comment

/**
 * This helper object is used to sort a list of pull request [Comment]s.
 * Since each comment can have a reply, we are dealing with a tree-like list.
 * To sort this list in a way that is usable in the UI, we use topological sorting.
 *
 * More details can be found here:
 * https://stackoverflow.com/questions/61021088
 */
object CommentSorter {

    private lateinit var groups: Map<Int?, List<Comment>>

    fun topologicalSort(comments: List<Comment>): List<Comment> {

        // create a map. The keys consist of all present parentIds
        // In other words: Each map entry is the root for another comment
        groups = comments
            .sortedWith(compareBy({ it.parentId }, { it.id }))
            .groupBy { it.parentId }

        // This is the actual sorting algorithm
        return comments.map { it.parentId }
            .subtract(comments.map { it.id })
            .flatMap { groups[it] ?: emptyList() }
            .flatMap { follow(it) }
    }

    // Recursively iterate over the comment children
    private fun follow(comment: Comment): List<Comment> {
        return listOf(comment) + (groups[comment.id] ?: emptyList()).flatMap { follow(it) }
    }
}