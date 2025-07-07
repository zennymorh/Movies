package com.zennymorh.movies.errorhandling

sealed class AppError {
    data object NetworkError : AppError()
    data object TimeoutError : AppError()
    data class ServerError(val code: Int, val message: String?) : AppError()
    data object UnknownError : AppError()
    data object EmptyResponseError : AppError()
    data object DatabaseError : AppError()
}
