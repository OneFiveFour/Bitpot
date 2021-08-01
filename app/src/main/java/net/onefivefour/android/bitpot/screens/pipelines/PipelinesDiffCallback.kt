package net.onefivefour.android.bitpot.screens.pipelines

import androidx.recyclerview.widget.DiffUtil
import net.onefivefour.android.bitpot.data.model.Pipeline

/**
 * This callback is used to calculate the difference of to lists of [Pipeline]s.
 * Be as specific as possible when comparing the contents to avoid strange behavior.
 */
class PipelinesDiffCallback : DiffUtil.ItemCallback<Pipeline>() {
    
    override fun areItemsTheSame(oldItem: Pipeline, newItem: Pipeline): Boolean {
        return oldItem.uuid == newItem.uuid
    }

    override fun areContentsTheSame(oldItem: Pipeline, newItem: Pipeline): Boolean {
        return arePipelinesTheSame(oldItem, newItem) 
    }

    private fun arePipelinesTheSame(oldItem: Pipeline, newItem: Pipeline): Boolean {
        return oldItem.buildNumber == newItem.buildNumber &&
                oldItem.createdOn == newItem.createdOn &&
                oldItem.state == newItem.state &&
                oldItem.target == newItem.target
    }
}