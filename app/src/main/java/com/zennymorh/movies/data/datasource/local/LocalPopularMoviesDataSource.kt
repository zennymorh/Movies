package com.zennymorh.movies.data.datasource.local

import com.zennymorh.movies.data.datasource.PopularMoviesDataSource
import javax.inject.Inject

class LocalPopularMoviesDataSource @Inject constructor() : PopularMoviesDataSource {
    override suspend fun getPopularMovies() {
        TODO("Not yet implemented")
    }
}
