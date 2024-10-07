package com.zennymorh.movies.data

import com.zennymorh.movies.data.datasource.PopularMoviesDataSource
import com.zennymorh.movies.data.model.PopularMovies
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PopularMoviesRepository {
    fun getPopularMoviesList(): Flow<List<PopularMovies>>
}

class PopularMoviesRepositoryImpl @Inject constructor(
    private val remotePopularMoviesDataSource: PopularMoviesDataSource,
//    private val localPopularMoviesDataSource: PopularMoviesDataSource,
): PopularMoviesRepository {
    override fun getPopularMoviesList(): Flow<List<PopularMovies>> {
        TODO("Not yet implemented")
    }

}