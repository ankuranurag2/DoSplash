package dev.ananurag2.dosplash.model

/**
 * created by ankur on 15/10/20
 */
sealed class Resource<T> {

    data class Success<T>(val data: T) : Resource<T>()

    data class Failure<T>(val message: String) : Resource<T>()

    companion object {

        fun <T> success(data: T) = Success(data)

        fun <T> error(message: String) = Failure<T>(message)
    }
}