package dev.ananurag2.dosplash.repository

import dev.ananurag2.dosplash.data.network.ApiService
import dev.ananurag2.dosplash.model.ImageResponse
import dev.ananurag2.dosplash.utils.decrypt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * created by ankur on 21/10/20
 */
class ImageRepository(private val apiService: ApiService) {

    suspend fun getLatestImages(pageNum: Int) = apiService.getLatestPhotos(pageNum, ACCESS_KEY)

    suspend fun getRandomImage() = apiService.getRandomPhoto(ACCESS_KEY)

    suspend fun getFilterList(query: String, originalList: ArrayList<ImageResponse>) = withContext(Dispatchers.Default) {
        originalList.filter { true == it.description?.contains(query) }
    }

    companion object {
        private val ENCRYPTION_KEY = "ENCRYPTION_KEY"
        private val ACCESS_KEY get() = "ACCESS_KEY".decrypt(ENCRYPTION_KEY)
    }
}