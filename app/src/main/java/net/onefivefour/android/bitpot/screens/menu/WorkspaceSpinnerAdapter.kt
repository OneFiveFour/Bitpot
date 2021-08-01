package net.onefivefour.android.bitpot.screens.menu

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import net.onefivefour.android.bitpot.data.model.Workspace

/**
 * Adapter for displaying the displayName of an user list in a Spinner
 */
class WorkspaceSpinnerAdapter(context: Context, textViewResourceId: Int, private val values: List<Workspace>) : ArrayAdapter<Workspace>(context, textViewResourceId, values) {

    override fun getCount() = values.size
    override fun getItem(position: Int) = values.elementAt(position)
    override fun getItemId(position: Int) = position.toLong()

    fun updateItems(newValues: List<Workspace>) {
        if (!isEmpty) clear()
        if (newValues.isNotEmpty()) addAll(newValues)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = values.elementAt(position).displayName
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = values.elementAt(position).displayName
        return label
    }
}