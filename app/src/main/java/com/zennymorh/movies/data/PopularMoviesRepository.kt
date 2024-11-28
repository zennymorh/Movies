package com.zennymorh.movies.data

import com.zennymorh.movies.data.datasource.PopularMoviesDataSource
import com.zennymorh.movies.data.model.Movie
import com.zennymorh.movies.errorhandling.ApiError
import com.zennymorh.movies.errorhandling.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface PopularMoviesRepository {
    suspend fun getPopularMoviesList(): Flow<Result<List<Movie>, ApiError>>
}

class PopularMoviesRepositoryImpl @Inject constructor(
    private val remotePopularMoviesDataSource: PopularMoviesDataSource,
): PopularMoviesRepository {
    override suspend fun getPopularMoviesList(): Flow<Result<List<Movie>, ApiError>> {
        return flow {
            when (val result = remotePopularMoviesDataSource.getPopularMovies()) {
                is Result.Success -> {
                    // Emit only the 'results' from PopularMovies
                    emit(Result.Success(result.data.results))
                }
                is Result.Error -> {
                    // Pass through the error result as is
                    emit(result)
                }
                else -> throw IllegalStateException("Unexpected result type: $result") // Handle unexpected states (if needed)

            }
        }

    }
}