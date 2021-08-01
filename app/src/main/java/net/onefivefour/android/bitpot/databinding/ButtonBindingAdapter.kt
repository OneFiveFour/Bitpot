package net.onefivefour.android.bitpot.databinding

import android.content.res.ColorStateList
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import net.onefivefour.android.bitpot.data.model.RepositoryColors


@BindingAdapter("iconTint")
fun setIconTint(button: MaterialButton, colors: RepositoryColors?) {
    if (colors == null) return
    val colorStateList = ColorStateList.valueOf(colors.textColor)
    button.iconTint = colorStateList
}

@BindingAdapter("outlineColor")
fun setOutlineColor(button: MaterialButton, colors: RepositoryColors?) {
    if (colors == null) return
    val colorStateList = ColorStateList.valueOf(colors.textColor)
    button.strokeColor = colorStateList
}