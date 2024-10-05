package com.zennymorh.movies.errorhandling

sealed class Result<out T : Any, out U : Any> {
    data class Success<out T : Any>(val data: T) : Result<T, Nothing>()
    data class Error<out U : Any>(val errorData: U) : Result<Nothing, U>()
    data object Loading : Result<Nothing, Nothing>()
}

data class ApiError(val code: Int, val message: String)
