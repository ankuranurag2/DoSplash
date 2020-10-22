package dev.ananurag2.dosplash.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ananurag2.dosplash.databinding.ListItemBinding
import dev.ananurag2.dosplash.model.ImageResponse
import dev.ananurag2.dosplash.utils.RecyclerViewEventListener

/**
 * created by ankur on 21/10/20
 */
class ImageListAdapter(private val listener: RecyclerViewEventListener) : ListAdapter<ImageResponse, ImageListAdapter.ImageVH>(ImageDiffUtilCallBack()) {

    inner class ImageVH(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageVH {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageVH(binding)
    }

    override fun onBindViewHolder(holder: ImageVH, position: Int) {
        val imageResponse = getItem(position)
        with(holder.binding) {
            image = imageResponse
            executePendingBindings()
            root.setOnClickListener { listener.onItemCLicked(imageResponse, ivPost, ivProfile) }
            if (position == itemCount - 1)
                listener.onBottomReached()
        }
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