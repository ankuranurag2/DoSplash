package dev.ananurag2.dosplash.utils

import dev.ananurag2.dosplash.model.ImageResponse

/**
 * created by ankur on 22/10/20
 */
interface RecyclerViewEventListener {
    fun onItemCLicked(imageResponse: ImageResponse)

    fun onBottomReached()
}