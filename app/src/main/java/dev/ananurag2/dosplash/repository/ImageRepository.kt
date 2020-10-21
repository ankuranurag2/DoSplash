package dev.ananurag2.dosplash.repository

import dev.ananurag2.dosplash.data.network.ApiService

/**
 * created by ankur on 21/10/20
 */
class ImageRepository(private val apiService: ApiService) {

    suspend fun getLatestImages(pageNum: Int) = apiService.getLatestPhotos(pageNum, ACCESS_KEY)

    suspend fun getRandomImage() = apiService.getRandomPhoto(ACCESS_KEY)

    companion object {
        //TODO : Encrypt or Remove
        private const val ACCESS_KEY = ""
    }
}