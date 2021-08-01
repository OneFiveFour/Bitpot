package net.onefivefour.android.bitpot.databinding

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.RepositoryAvatar
import net.onefivefour.android.bitpot.data.model.RepositoryColors
import net.onefivefour.android.bitpot.data.model.WebHook
import net.onefivefour.android.bitpot.data.model.WebHookEvent
import net.onefivefour.android.bitpot.extensions.getThemeColor
import net.onefivefour.android.bitpot.extensions.setGradient
import net.onefivefour.android.bitpot.model.RoundEdgePosition


/**
 * For [RepositoryAvatar.Language] avatars, the Icon gets tinted in the text color.
 */
@BindingAdapter("avatarTint")
fun setAvatarTint(imageView: ImageView, avatar: RepositoryAvatar?) {
    if (avatar == null) return

    when (avatar) {
        is RepositoryAvatar.Language -> {
            val tintColor = ContextCompat.getColor(imageView.context, avatar.textColor)
            imageView.imageTintList = ColorStateList.valueOf(tintColor)
        }
        is RepositoryAvatar.Image -> imageView.imageTintList = null
    }
}

/**
 * sets the background, icon tinting and item text color for the BottomNavigationView
 */
@BindingAdapter("bottomNavColors")
fun setBottomNavColors(navView: BottomNavigationView, colors: RepositoryColors?) {
    if (colors == null) return

    val colorStateList = ColorStateList.valueOf(colors.textColor)
    navView.itemIconTintList = colorStateList
    navView.itemTextColor = colorStateList

    navView.setGradient(colors, RoundEdgePosition.TOP)
}

/**
 * sets the Views background gradient according to the given [RepositoryColors]
 */
@BindingAdapter("gradientBackground")
fun setBackgroundGradient(view: View, colors: RepositoryColors?) {
    if (colors == null) {
        val backgroundColor = view.context.getThemeColor(R.attr.colorSurface)
        view.setBackgroundColor(backgroundColor)
        return
    }

    view.setGradient(colors)
}


@BindingAdapter("notificationStatus")
fun setNotificationStatus(imageView: ImageView, webHook: WebHook?) {
    if (webHook == null || webHook.events.isEmpty()) {
        // no webHook defined
        imageView.setImageResource(R.drawable.ic_notifications_off)
        return
    }

    // check for all or some notifications
    val numberOfPossibleHookEvents = listOf(WebHookEvent.PULL_REQUEST_UPDATES, WebHookEvent.PIPELINE_UPDATES).size
    val allHooksActive = numberOfPossibleHookEvents == webHook.events.size
    val drawableRes = when {
        allHooksActive -> R.drawable.ic_notifications_all
        else -> R.drawable.ic_notifications_some
    }
    imageView.setImageResource(drawableRes)
}