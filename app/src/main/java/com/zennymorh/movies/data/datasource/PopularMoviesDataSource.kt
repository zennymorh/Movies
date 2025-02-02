package com.zennymorh.movies.data.datasource

import com.zennymorh.movies.data.model.PopularMovies


interface PopularMoviesDataSource {
    suspend fun getPopularMovies()
}