package ru.nsu.merkuriev.waterbalance.utils.ui

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:iconColor")
fun setIconColor(view: ImageView, color: Int?) {
    if (color != null && 0 != color) {
        view.setColorFilter(ContextCompat.getColor(view.context, color))
    }
}