package dev.ananurag2.dosplash.model

import android.os.Parcelable
import androidx.annotation.Keep

import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


/**
 * created by ankur on 21/10/20
 */
@Keep
@Parcelize
data class ImageResponse(
    @Json(name = "id")
    val id: String?,
    @Json(name = "description")
    val desc: String?,
    @Json(name = "alt_description")
    val alternateDesc: String?,
    @Json(name = "user")
    val user: User?,
    @Json(name = "urls")
    val urls: Urls?
) :Parcelable{
    val description get() = if (desc.isNullOrBlank()) alternateDesc else desc
}

@Keep
@Parcelize
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
):Parcelable

@Keep
@Parcelize
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
):Parcelable

@Keep
@Parcelize
data class ProfileImage(
    @Json(name = "small")
    val small: String?,
    @Json(name = "medium")
    val medium: String?,
    @Json(name = "large")
    val large: String?
):Parcelable

