package dev.ananurag2.dosplash.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * created by ankur on 25/10/20
 */
@Keep
@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "errors")
    val errorList: List<String>?
)