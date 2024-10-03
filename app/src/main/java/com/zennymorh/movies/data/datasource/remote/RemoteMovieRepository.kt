package com.zennymorh.movies.data.datasource.remote

import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.model.PopularMovies
import com.zennymorh.movies.errorhandling.AppError
import com.zennymorh.movies.errorhandling.Result
import javax.inject.Inject

interface RemoteMovieRepository {
    suspend fun getMovies(): Result<PopularMovies, AppError>
}

class RemoteMovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): RemoteMovieRepository {
    override suspend fun getMovies(): Result<PopularMovies, AppError> {

    }
}