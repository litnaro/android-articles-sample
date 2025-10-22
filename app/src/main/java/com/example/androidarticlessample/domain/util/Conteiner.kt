package com.example.androidarticlessample.domain.util

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Container<out T> {

    data object Pending : Container<Nothing>()

    class Success<T>(val data: T) : Container<T>()

    class Error<T>(val exception: AppException) : Container<T>()

    fun <R> map(mapper: Mapper<T, R>? = null) : Container<R> {
        return when(this) {
            is Pending -> Pending
            is Error -> Error(exception)
            is Success -> {
                if (mapper == null) throw IllegalArgumentException("Mapper should not be null for success result")
                Success(mapper(this.data))
            }
        }
    }
}

@Suppress("unused", "UNREACHABLE_CODE")
fun <T> Container<T>?.successResult() : T? {
    return if (this is Container.Success) {
        return this.data
    } else {
        return null
    }
}