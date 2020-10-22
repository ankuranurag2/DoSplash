package dev.ananurag2.dosplash.repository

import dev.ananurag2.dosplash.data.network.ApiService
import dev.ananurag2.dosplash.utils.decrypt

/**
 * created by ankur on 21/10/20
 */
class ImageRepository(private val apiService: ApiService) {

    suspend fun getLatestImages(pageNum: Int) = apiService.getLatestPhotos(pageNum, ACCESS_KEY)

    suspend fun getRandomImage() = apiService.getRandomPhoto(ACCESS_KEY)

    companion object {
        private val ENCRYPTION_KEY = "YOUR_ENCRYPTION_KEY"
        private val ACCESS_KEY get() = "ENCRYPTED_UNSPLASH_ACCESS_KEY".decrypt(ENCRYPTION_KEY)
    }
}