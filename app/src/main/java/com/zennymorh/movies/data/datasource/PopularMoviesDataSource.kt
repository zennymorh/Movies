package com.zennymorh.movies.data.datasource

import com.zennymorh.movies.data.model.PopularMovies
import com.zennymorh.movies.errorhandling.ApiError
import com.zennymorh.movies.errorhandling.Result

interface PopularMoviesDataSource {
    suspend fun getPopularMovies(): Result<PopularMovies, ApiError>
}