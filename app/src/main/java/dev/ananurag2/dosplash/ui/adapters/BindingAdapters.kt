package dev.ananurag2.dosplash.ui.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dev.ananurag2.dosplash.R

/**
 * created by ankur on 21/10/20
 * Custom binding adapter for ImageView
 * It will take two urls i.e {@value thumbUrl} and {@value mainUrl}.
 * The `thumbUrl` will be used to load a low quality image until the high quality image is fetched using `mainUrl`.
 */
@BindingAdapter(value = ["thumbUrl", "mainUrl"], requireAll = true)
fun loadThumbnailAndImage(view: ImageView, thumbUrl: String?, mainUrl: String?) {
    if (mainUrl.isNullOrBlank()) return
    val thumbnailRequest = Glide.with(view.context)
        .load(thumbUrl)

    Glide.with(view.context)
        .load(mainUrl)
        .thumbnail(thumbnailRequest)
        .placeholder(R.drawable.ic_loading)
        .error(R.drawable.ic_error)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .apply(RequestOptions().dontTransform())
        .into(view)
}

/**
 * BindingAdapter that avoids null OR blank values on the TextView
 */
@BindingAdapter("safeText", requireAll = true)
fun setNonNullText(view: TextView, text: String?) {
    view.text = if (text.isNullOrBlank()) "Not Available" else text
}