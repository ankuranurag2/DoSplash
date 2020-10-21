package dev.ananurag2.dosplash.ui.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dev.ananurag2.dosplash.R

/**
 * created by ankur on 21/10/20
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