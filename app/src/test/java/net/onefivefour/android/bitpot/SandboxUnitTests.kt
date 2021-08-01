package net.onefivefour.android.bitpot

import org.junit.Test
import timber.log.Timber


data class Comment(
    val id: Int,
    val parentId: Int?
)

class SandboxUnitTests {

    @Test
    fun sandboxTest() {

        val comments = listOf(
            Comment(6, 5),
            Comment(4, 1),
            Comment(2, null),
            Comment(1, null),
            Comment(5, null),
            Comment(3, 1),
            Comment(7, 2),
            Comment(8, 4)
        )


        val sorted = sortComments(comments)

        Timber.d(sorted.toString())

    }

    private lateinit var groups: Map<Int?, List<Comment>>

    private fun sortComments(comments: List<Comment>): List<Comment> {

        // Get all of the parent groups
        val groups = comments
            .sortedWith(compareBy({ it.parentId }, { it.id }))
            .groupBy { it.parentId }

        // Run the follow method on each of the roots
        comments.map { it.parentId }
            .subtract(comments.map { it.id })
            .flatMap { groups[it] ?: emptyList() }
            .flatMap { follow(it) }
            .forEach(System.out::println)



        return comments

    }

    // Recursively get the children
    private fun follow(comment: Comment): List<Comment> {
        return listOf(comment) + (groups[comment.id] ?: emptyList()).flatMap { follow(it) }
    }


}





