package com.zennymorh.movies.datasource.remote

import com.zennymorh.movies.api.ApiService
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getMovies() = apiService.getPopularMovies()
}