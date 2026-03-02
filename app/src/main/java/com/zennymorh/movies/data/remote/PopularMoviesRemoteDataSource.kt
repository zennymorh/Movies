package com.zennymorh.movies.data.remote

import com.zennymorh.movies.data.error.AppError
import com.zennymorh.movies.data.error.AppException
import com.zennymorh.movies.data.remote.api.ApiService
import com.zennymorh.movies.domain.model.PopularMovies
import retrofit2.Response
import javax.inject.Inject

interface PopularMoviesRemoteDataSource {
    suspend fun getPopularMovies(page: Int): PopularMovies
}

class DefaultPopularMoviesRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : PopularMoviesRemoteDataSource {

    override suspend fun getPopularMovies(page: Int): PopularMovies {
        val response = apiService.getPopularMovies(page)

        if (response.isSuccessful) {
            return response.body()
                ?: throw AppException(AppError.UnknownError)
        } else {
            throw AppException(
                AppError.ServerError(response.code(), response.message())
            )
        }
    }
}