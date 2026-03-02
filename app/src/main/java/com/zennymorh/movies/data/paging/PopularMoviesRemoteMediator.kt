package com.zennymorh.movies.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.zennymorh.movies.data.local.PopularMovieDatabase
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.data.model.RemoteKey
import com.zennymorh.movies.data.remote.api.ApiService
import com.zennymorh.movies.network.AppError
import com.zennymorh.movies.network.AppException
import java.io.IOException
import java.net.SocketTimeoutException

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator(
    private val apiService: ApiService,
    private val database: PopularMovieDatabase
) : RemoteMediator<Int, PopularMovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularMovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val response = apiService.getPopularMovies(page = page)
            
            if (response.isSuccessful) {
                val apiResponse = response.body()
                val movies = apiResponse?.results?.map { movie ->
                    PopularMovieEntity(
                        id = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        posterPath = movie.posterPath,
                        releaseDate = movie.releaseDate
                    )
                } ?: emptyList()

                val endOfPaginationReached = movies.isEmpty() || 
                    (apiResponse?.page ?: page) >= (apiResponse?.totalPages ?: Int.MAX_VALUE)

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.remoteKeyDao().clearRemoteKeys()
                        database.movieDao().clearAll()
                    }
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = movies.map {
                        RemoteKey(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    database.remoteKeyDao().insertAll(keys)
                    database.movieDao().insertMovies(movies)
                }
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
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

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PopularMovieEntity>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                database.remoteKeyDao().getRemoteKeysForMovie(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PopularMovieEntity>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                database.remoteKeyDao().getRemoteKeysForMovie(movie.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PopularMovieEntity>
    ): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                database.remoteKeyDao().getRemoteKeysForMovie(movieId)
            }
        }
    }
}
