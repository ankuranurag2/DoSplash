package dev.ananurag2.dosplash.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ananurag2.dosplash.databinding.ListItemBinding
import dev.ananurag2.dosplash.model.ImageResponse

/**
 * created by ankur on 21/10/20
 */
class ImageListAdapter : ListAdapter<ImageResponse, ImageListAdapter.ImageVH>(ImageDiffUtilCallBack()) {

    inner class ImageVH(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageVH {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageVH(binding)
    }

    override fun onBindViewHolder(holder: ImageVH, position: Int) {
        holder.binding.image = getItem(position)
        holder.binding.executePendingBindings()
    }
}

//To determine if the Adapter needs to redraw the items
private class ImageDiffUtilCallBack : DiffUtil.ItemCallback<ImageResponse>() {
    override fun areItemsTheSame(oldItem: ImageResponse, newItem: ImageResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageResponse, newItem: ImageResponse): Boolean {
        return oldItem == newItem
    }
}