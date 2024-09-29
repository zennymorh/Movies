package com.zennymorh.movies.data.datasource.remote

import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.model.PopularMovies
import javax.inject.Inject

interface MovieRepository {
    suspend fun getMovies(): PopularMovies
}

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): MovieRepository {
    override suspend fun getMovies() = apiService.getPopularMovies()
}