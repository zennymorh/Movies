package com.zennymorh.movies.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.network.AppError
import com.zennymorh.movies.network.AppException
import com.zennymorh.movies.roomdb.PopularMovieDao
import com.zennymorh.movies.roomdb.PopularMovieDatabase
import java.io.IOException
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
            val page: Int? = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> null
                LoadType.APPEND -> {
                    val lastMovie = state.lastItemOrNull()
                    if (lastMovie == null) {
                        null
                    } else {
                        // Assuming 20 items per page based on Repository config
                        (state.pages.sumOf { it.data.size } / 20) + 1
                    }
                }
            }

            if (page == null) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val response = apiService.getPopularMovies(page = page)
            
            if (response.isSuccessful) {
                val movies = response.body()?.results?.map { movie ->
                    PopularMovieEntity(
                        id = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        posterPath = movie.posterPath,
                        releaseDate = movie.releaseDate
                    )
                } ?: emptyList()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        movieDao.clearAll()
                    }
                    movieDao.insertMovies(movies)
                }
                MediatorResult.Success(endOfPaginationReached = movies.isEmpty())
            } else {
                MediatorResult.Error(AppException(AppError.ServerError(response.code(), response.message())))
            }
        } catch (e: SocketTimeoutException) {
            MediatorResult.Error(AppException(AppError.TimeoutError))
        } catch (e: IOException) {
            MediatorResult.Error(AppException(AppError.NetworkError))
        } catch (e: Exception) {
            MediatorResult.Error(AppException(AppError.UnknownError))
        }
    }
}
