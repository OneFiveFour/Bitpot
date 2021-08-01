package net.onefivefour.android.bitpot.customviews.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.CompoundButton
import android.widget.Switch
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreference
import net.onefivefour.android.bitpot.R

/**
 * A custom Preference for boolean switches
 */
class BitpotSwitchPreference : SwitchPreference {

    private val listener = Listener()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val switchView = holder.findViewById(R.id.sw_settings)
        setSwitchView(switchView)
        syncSummaryView(holder)
    }

    private fun setSwitchView(view: View) {
        if (view is Switch) {
            view.setOnCheckedChangeListener(null)
        }
        if (view is Checkable) {
            (view as Checkable).isChecked = mChecked
        }
        if (view is Switch) {
            view.setOnCheckedChangeListener(listener)
        }
    }

    /**
     * This listener is triggered whenever the [BitpotSwitchPreference] changes.
     */
    inner class Listener internal constructor() : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            this@BitpotSwitchPreference.isChecked = isChecked
        }
    }
}