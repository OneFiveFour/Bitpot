package net.onefivefour.android.bitpot.data.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * A complete listing of all possible language selections of a Bitbucket repository.
 * Each repository language has its associated drawable XOR avatar.
 */
sealed class RepositoryAvatar {

    /**
     * Used for a repository without a custom image. Depending on its
     * programming language the avatar is chosen. Also the background gradient and
     * the text color for this gradient can be found in this class.
     */
    class Language(
        @DrawableRes val drawable: Int,
        @ColorRes val gradientFromColor: Int,
        @ColorRes val gradientToColor: Int,
        @ColorRes val textColor: Int
    ) : RepositoryAvatar()

    /**
     * Used for a repository with a custom image. The URL for this image
     * is used for the avatar.
     */
    class Image(val url: String) : RepositoryAvatar()
}