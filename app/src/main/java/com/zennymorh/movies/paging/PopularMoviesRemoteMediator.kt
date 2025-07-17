package com.zennymorh.movies.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.errorhandling.AppError
import com.zennymorh.movies.roomdb.PopularMovieDao
import com.zennymorh.movies.roomdb.PopularMovieDatabase
import java.io.IOException // Import IOException for a more specific catch if desired
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
        val mediatorResult: MediatorResult = try {
            val page: Int? = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> null // Signal to return success immediately
                LoadType.APPEND -> {
                    val lastMovie = state.lastItemOrNull()
                    if (lastMovie == null) {
                        null // Signal to return success immediately
                    } else {
                        // Assuming 'id' in PopularMovieEntity is suitable for page calculation.
                        // If 'id' is not sequential or page-related, this logic might need adjustment
                        // or you might need to store and retrieve a RemoteKey for the next page.
                        // For this example, we'll keep the existing logic.
                        (lastMovie.id / state.config.pageSize) + 1
                    }
                }
            }

            if (page == null) { // Handles PREPEND or APPEND with no last item
                MediatorResult.Success(endOfPaginationReached = true)
            } else {
                // Use Kotlin Result to fetch API data
                val apiResult = fetchMovies(page)

                apiResult.fold(
                    success = { movies ->
                        database.withTransaction {
                            if (loadType == LoadType.REFRESH) {
                                movieDao.clearAll()
                            }
                            movieDao.insertMovies(movies)
                        }
                        MediatorResult.Success(endOfPaginationReached = movies.isEmpty())
                    },
                    failure = { appError ->
                        // Map AppError to a specific Exception if needed, or use a generic one
                        MediatorResult.Error(mapAppErrorToException(appError))
                    }
                )
            }
        } catch (e: IOException) { // Example: Catching IOExceptions (covers SocketTimeoutException)
            Log.e("RemoteMediator", "IO Error in load: ${e.message}", e)
            MediatorResult.Error(e)
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) { // Catch any other unexpected exceptions
            Log.e("RemoteMediator", "Unexpected Error in load: ${e.message}", e)
            MediatorResult.Error(e)
        }
        return mediatorResult // First return statement
    }

    // Helper function to map AppError to Exception
    private fun mapAppErrorToException(appError: AppError): Exception {
        return Exception(appError.toString()) // Simple mapping, can be more sophisticated
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
            Log.e("PopularMoviesRemoteMediator", "Timeout error", e)
            Err(AppError.TimeoutError)
        } catch (e: IOException) { // More specific than just Exception for network issues
            Log.e("PopularMoviesRemoteMediator", "IO error fetching movies", e)
            Err(AppError.NetworkError) // Assuming you have an AppError.IOError
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            Log.e("PopularMoviesRemoteMediator", "Error fetching movies", e)
            Err(AppError.UnknownError)
        }
    }
}
