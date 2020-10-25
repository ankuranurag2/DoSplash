package dev.ananurag2.dosplash.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dev.ananurag2.dosplash.R

/**
 * created by ankur on 25/10/20
 */

/**
 * Extension method to provide Glide callback, so that postponedTransitions are resumed
 */
inline fun ImageView.loadWithCompletionCallBack(thumbUrl: String?, mainUrl: String?, crossinline onLoadingDone: () -> Unit = {}) {
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            onLoadingDone()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?, model: Any?, target: Target<Drawable>?,
            dataSource: DataSource?, isFirstResource: Boolean
        ): Boolean {
            onLoadingDone()
            return false
        }
    }

    val thumbnailRequest = Glide.with(this.context)
        .load(thumbUrl)

    val options: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_loading)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .error(R.drawable.ic_error)

    Glide.with(this.context)
        .load(mainUrl)
        .thumbnail(thumbnailRequest)
        .apply(options)
        .listener(listener)
        .into(this)
}