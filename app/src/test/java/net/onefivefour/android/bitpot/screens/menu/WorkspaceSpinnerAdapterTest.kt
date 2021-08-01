@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection")

package net.onefivefour.android.bitpot.screens.menu

import android.content.Context
import io.mockk.mockk
import net.onefivefour.android.bitpot.data.model.Workspace
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WorkspaceSpinnerAdapterTest {

    private lateinit var sut: WorkspaceSpinnerAdapter

    private val fakeValues: ArrayList<Workspace> = getFakeValues()

    private val context: Context = mockk()

    @Before
    fun setUp() {
        val fakeTextViewResourceId = 0
        sut = WorkspaceSpinnerAdapter(context, fakeTextViewResourceId, fakeValues)
    }

    @Test
    fun getCount_returnsCorrectSize() {
        val itemCount = sut.count
        assertEquals(fakeValues.size, itemCount)
    }

    @Test
    fun getItem_returnsCorrectItem() {
        val item = sut.getItem(0)
        assertEquals(fakeValues.toList()[0], item)
    }

    @Test
    fun getItemId_returnsPositionAsLong() {
        val itemId = sut.getItemId(0)
        assertEquals(0.toLong(), itemId)
    }

    private fun getFakeValues(): ArrayList<Workspace> {
        return arrayListOf(
            Workspace("user.id", "user", "avatarUrl"),
            Workspace("user.id","user", "avatarUrl2")
        )
    }
}