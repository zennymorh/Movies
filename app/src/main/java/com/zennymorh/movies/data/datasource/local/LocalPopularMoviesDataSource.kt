package com.zennymorh.movies.data.datasource.local

import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.datasource.PopularMoviesDataSource
import com.zennymorh.movies.data.model.PopularMovies
import com.zennymorh.movies.errorhandling.ApiError
import com.zennymorh.movies.errorhandling.Result
import javax.inject.Inject

class LocalPopularMoviesDataSource @Inject constructor(
    private val apiService: ApiService
): PopularMoviesDataSource {
    override suspend fun getPopularMovies(): Result<PopularMovies, ApiError> {
        TODO("Not yet implemented")
    }

}