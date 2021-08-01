package net.onefivefour.android.bitpot.screens.repositories

import androidx.recyclerview.widget.DiffUtil
import com.squareup.picasso.Picasso
import net.onefivefour.android.bitpot.data.model.Repository
import net.onefivefour.android.bitpot.data.model.RepositoryAvatar

/**
 * This callback is used to calculate the difference of to lists of [Repository]s.
 * Be as specific as possible when comparing the contents to avoid strange behavior.
 */
class RepositoriesDiffCallback : DiffUtil.ItemCallback<Repository>() {
    
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return  oldItem.uuid == newItem.uuid
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return areRepositoriesTheSame(oldItem, newItem)   
    }

    private fun areRepositoriesTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return isSameLastUpdate(oldItem, newItem) && // this check needs to be done first to catch avatar changes
                oldItem.isPrivate == newItem.isPrivate &&
                oldItem.name == newItem.name &&
                oldItem.lastPipelineState == newItem.lastPipelineState &&
                isSameAvatar(oldItem.avatar, newItem.avatar)
    }

    private fun isSameLastUpdate(oldItem: Repository, newItem: Repository): Boolean {
        if (oldItem.lastUpdated.epochSecond == newItem.lastUpdated.epochSecond) return true

        val avatar = oldItem.avatar
        if (avatar is RepositoryAvatar.Image) Picasso.get().invalidate(avatar.url)

        return false
    }


    private fun isSameAvatar(oldItemAvatar: RepositoryAvatar, newItemAvatar: RepositoryAvatar): Boolean {
        // check for different avatar types
        if (oldItemAvatar is RepositoryAvatar.Image && newItemAvatar is RepositoryAvatar.Language) return false
        if (oldItemAvatar is RepositoryAvatar.Language && newItemAvatar is RepositoryAvatar.Image) return false

        // check for different languages
        if (oldItemAvatar is RepositoryAvatar.Language && newItemAvatar is RepositoryAvatar.Language) {
            return oldItemAvatar.drawable == newItemAvatar.drawable
        }

        // check for different avatar urls
        if (oldItemAvatar is RepositoryAvatar.Image && newItemAvatar is RepositoryAvatar.Image) {
            return oldItemAvatar.url == newItemAvatar.url
        }

        // fallback result false
        return false
    }
}