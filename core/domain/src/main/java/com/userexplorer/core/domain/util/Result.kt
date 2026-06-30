package com.userexplorer.core.domain.util

/**
 * A sealed class representing the result of an asynchronous operation.
 * Used to handle success, error, and loading states in a type-safe way.
 *
 * @param T The type of data that will be returned in case of success
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation.
     *
     * @param T The type of the data
     * @property data The data returned by the operation
     */
    data class Success<T>(val data: T) : Result<T>()

    /**
     * Represents a failed operation.
     *
     * @property message Description of what went wrong
     */
    data class Error(val message: String) : Result<Nothing>()

    /**
     * Represents an ongoing operation.
     */
    object Loading : Result<Nothing>()
} 