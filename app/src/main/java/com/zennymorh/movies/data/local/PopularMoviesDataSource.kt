package com.zennymorh.movies.data.local

interface PopularMoviesDataSource {
    suspend fun getPopularMovies()
}
