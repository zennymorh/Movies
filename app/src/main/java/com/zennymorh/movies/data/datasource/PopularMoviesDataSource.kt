package com.zennymorh.movies.data.datasource

interface PopularMoviesDataSource {
    suspend fun getPopularMovies()
}
