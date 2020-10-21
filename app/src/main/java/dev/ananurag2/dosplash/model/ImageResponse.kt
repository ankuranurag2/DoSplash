package dev.ananurag2.dosplash.model
import androidx.annotation.Keep

import com.squareup.moshi.Json


/**
 * created by ankur on 21/10/20
 */
@Keep
data class ImageResponse(
    @Json(name = "id")
    val id: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "user")
    val user: User?,
    @Json(name = "urls")
    val urls: Urls?
)

@Keep
data class User(
    @Json(name = "id")
    val id: String?,
    @Json(name = "username")
    val username: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "portfolio_url")
    val portfolioUrl: String?,
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "location")
    val location: String?,
    @Json(name = "profile_image")
    val profileImage: ProfileImage?
)

@Keep
data class Urls(
    @Json(name = "raw")
    val raw: String?,
    @Json(name = "full")
    val full: String?,
    @Json(name = "regular")
    val regular: String?,
    @Json(name = "small")
    val small: String?,
    @Json(name = "thumb")
    val thumb: String?
)

@Keep
data class ProfileImage(
    @Json(name = "small")
    val small: String?,
    @Json(name = "medium")
    val medium: String?,
    @Json(name = "large")
    val large: String?
)

