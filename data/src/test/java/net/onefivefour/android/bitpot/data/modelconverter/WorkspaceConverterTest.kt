package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.meta.dummies.WorkspaceDummies
import net.onefivefour.android.bitpot.data.model.converter.WorkspaceConverter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class WorkspaceConverterTest {

    private val workspacesDummy = WorkspaceDummies.getNetworkWorkspaces()

    private lateinit var sut: WorkspaceConverter

    @Before
    fun setup() {
        sut = WorkspaceConverter()
    }

    @Test
    fun convert_simpleUser_returnsConvertedUser() {
        val result = sut.toAppModel(workspacesDummy)
        
        val firstWorkspace = result.first()

        assertEquals("https://avatarlink.com", firstWorkspace.avatarUrl)
        assertEquals("Florian Meyer", firstWorkspace.displayName)
        assertEquals("Workspace.uuid", firstWorkspace.uuid)
    }
}