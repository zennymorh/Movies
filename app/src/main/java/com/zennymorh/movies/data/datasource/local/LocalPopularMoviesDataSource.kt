package com.zennymorh.movies.data.datasource.local

import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.datasource.PopularMoviesDataSource
import com.zennymorh.movies.data.model.PopularMovies

import javax.inject.Inject

class LocalPopularMoviesDataSource @Inject constructor(
    private val apiService: ApiService
): PopularMoviesDataSource {
    override suspend fun getPopularMovies() {
        TODO("Not yet implemented")
    }

}