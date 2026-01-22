package com.zennymorh.movies.network

sealed class AppError {
    data object NetworkError : AppError()
    data object TimeoutError : AppError()
    data class ServerError(val code: Int, val message: String?) : AppError()
    data object UnknownError : AppError()
    data object EmptyResponseError : AppError()
    data object DatabaseError : AppError()
}

/**
 * A custom exception that carries an [AppError].
 * This allows us to pass structured error information through APIs that expect a Throwable,
 * such as Paging 3's LoadState.
 */
class AppException(val error: AppError) : Exception()

fun Throwable.toAppError(): AppError {
    return when (this) {
        is AppException -> this.error
        else -> AppError.UnknownError
    }
}
