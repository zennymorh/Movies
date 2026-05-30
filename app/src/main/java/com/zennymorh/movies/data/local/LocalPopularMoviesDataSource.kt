package com.zennymorh.movies.data.local

import javax.inject.Inject

class LocalPopularMoviesDataSource @Inject constructor() : PopularMoviesDataSource {
    override suspend fun getPopularMovies() {
        TODO("Not yet implemented")
    }
}