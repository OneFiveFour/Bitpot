package net.onefivefour.android.bitpot.databinding

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.model.Workspace
import net.onefivefour.android.bitpot.screens.menu.WorkspaceSpinnerAdapter


/**
 * Used to fill the workspaces Spinner with items
 */
@BindingAdapter("workspaces")
fun setWorkspaces(spinner: Spinner, workspaces: List<Workspace>?) {
    if (workspaces == null) return

    // update adapter with new workspaces
    (spinner.adapter as WorkspaceSpinnerAdapter).updateItems(workspaces)

    // If found, select the last used workspace.
    val selectedWorkspaceUuid = BitpotData.getSelectedWorkspaceUuid()
    for (index in 0 until spinner.adapter.count) {
        val currentItem = spinner.getItemAtPosition(index) as Workspace
        if (currentItem.uuid == selectedWorkspaceUuid) {
            spinner.setSelection(index)
            return
        }
    }
}