package dev.ananurag2.dosplash.model

/**
 * created by ankur on 15/10/20
 * A class to encapsulate the Response as either {@property success} or {@property error}
 */
sealed class Resource<T> {

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(val message: String) : Resource<T>()

    companion object {

        fun <T> success(data: T) = Success(data)

        fun <T> error(message: String) = Error<T>(message)
    }
}