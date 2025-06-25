package com.zennymorh.movies.data.datasource.remote

import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.datasource.PopularMoviesDataSource
import javax.inject.Inject

class RemotePopularMoviesDataSourceImpl @Inject constructor(
    private val apiService: ApiService
): PopularMoviesDataSource {
    override suspend fun getPopularMovies() {

    }
}