package com.example.androidarticlessample.domain.util

/**
 * Represents application-level exceptions that describe known error cases.
 */
sealed class AppException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    class Network(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class Server(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class NotFound(message: String? = null) : AppException(message)
    class Invalid(message: String? = "Invalid input", cause: Throwable? = null) : AppException(message, cause)
    class Unknown(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
}