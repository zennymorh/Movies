package com.zennymorh.movies.data.datasource.remote

import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.datasource.PopularMoviesDataSource
import com.zennymorh.movies.data.model.PopularMovies
import com.zennymorh.movies.errorhandling.ApiError
import com.zennymorh.movies.errorhandling.Result
import javax.inject.Inject

class RemotePopularMoviesDataSourceImpl @Inject constructor(
    private val apiService: ApiService
): PopularMoviesDataSource {
    override suspend fun getPopularMovies(): Result<PopularMovies, ApiError> {
        return try {
            val response = apiService.getPopularMovies()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error(ApiError(-1, "Empty response body"))
            } else {
                Result.Error(ApiError(response.code(), response.message()))
            }
        } catch (e: Exception) {
            Result.Error(ApiError(-1, e.message ?: "Unknown error"))
        }
    }
}