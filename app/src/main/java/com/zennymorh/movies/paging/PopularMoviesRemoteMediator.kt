package com.zennymorh.movies.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.errorhandling.AppError
import com.zennymorh.movies.roomdb.PopularMovieDao
import com.zennymorh.movies.roomdb.PopularMovieDatabase
import java.io.IOException
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import java.net.SocketTimeoutException

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator(
    private val movieDao: PopularMovieDao,
    private val apiService: ApiService,
    private val database: PopularMovieDatabase
) : RemoteMediator<Int, PopularMovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularMovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastMovie = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    (lastMovie.id / state.config.pageSize) + 1
                }
            }

            // Use Kotlin Result to fetch API data
            val result = fetchMovies(page)

            result.fold(
                success = { movies ->
                    database.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            movieDao.clearAll()
                        }
                        movieDao.insertMovies(movies)
                    }
                    MediatorResult.Success(endOfPaginationReached = movies.isEmpty())
                },
                failure = { error ->
                    MediatorResult.Error(Exception(error.toString()))
                }
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    // Fetch movies and return Result<List<Movie>, AppError>
    private suspend fun fetchMovies(page: Int): Result<List<PopularMovieEntity>, AppError> {
        return try {
            val response = apiService.getPopularMovies(page = page)
            if (response.isSuccessful) {
                response.body()?.results?.map { movie ->
                    PopularMovieEntity(
                        id = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        posterPath = movie.posterPath,
                        releaseDate = movie.releaseDate
                    )
                }?.let { Ok(it) } ?: Err(AppError.EmptyResponseError)
            } else {
                Err(AppError.ServerError(response.code(), response.message()))
            }
        } catch (e: SocketTimeoutException) {
            Err(AppError.TimeoutError)
        } catch (e: Exception) {
            Err(AppError.UnknownError)
        }
    }
}
