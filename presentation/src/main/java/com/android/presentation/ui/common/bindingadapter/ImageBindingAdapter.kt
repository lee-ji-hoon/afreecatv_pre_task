package com.android.presentation.ui.common.bindingadapter

import android.os.Build.VERSION.SDK_INT
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (imageUrl.isNullOrEmpty().not()) {
        view.load(imageUrl)
    }
}

@BindingAdapter("circleImageUrl")
fun loadCircleImage(view: ImageView, imageUrl: String?) {
    val imageLoader = ImageLoader.Builder(view.context).components {
        if (SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()

    imageUrl?.let {
        view.load(it, imageLoader)
    }
}
