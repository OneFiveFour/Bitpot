package net.onefivefour.android.bitpot.databinding

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import net.onefivefour.android.bitpot.common.CircleTransformation
import net.onefivefour.android.bitpot.data.model.RepositoryAvatar

@BindingAdapter("imageTint")
fun setTint(imageView: ImageView, @ColorInt color: Int) {
    ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color))
}

/**
 * Used to set either a vector drawable or an url to load the
 * avatar of an repository.
 */
@BindingAdapter("repositoryAvatar")
fun setRepositoryAvatar(imageView: ImageView, avatar: RepositoryAvatar?) {
    if (avatar == null) {
        imageView.setImageResource(android.R.color.transparent)
        return
    }

    when (avatar) {
        is RepositoryAvatar.Language -> imageView.setImageResource(avatar.drawable)
        is RepositoryAvatar.Image -> setCircledImage(imageView, avatar.url)
    }
}

@BindingAdapter("circleImageUrl")
fun setCircleImageUrl(imageView: ImageView, avatarUrl: String?) {
    if (avatarUrl.isNullOrEmpty()) return
    setCircledImage(imageView, avatarUrl)
}

/**
 * creates a round ImageView and fills it with the image behind the given url.
 */
private fun setCircledImage(imageView: ImageView, url: String) {
    Picasso.get()
        .load(url)
        .networkPolicy(NetworkPolicy.NO_CACHE)
        .fit()
        .transform(CircleTransformation())
        .into(imageView)
}

