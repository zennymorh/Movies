package com.zennymorh.movies.errorhandling

sealed class Result<out S, out E> {
    data class Success<out S>(val data: S) : Result<S, Nothing>()
    data class Error<out E>(val error:E) : Result<Nothing, E>()
}

sealed class AppError{
    data object NetworkError : AppError()
    data object ServerError : AppError()
    data object ExternalServiceError : AppError()
    // Add more specific error types as needed
}
