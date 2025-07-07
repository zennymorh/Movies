package com.zennymorh.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.errorhandling.AppError
import com.zennymorh.movies.paging.PopularMoviesRemoteMediator
import com.zennymorh.movies.roomdb.PopularMovieDao
import com.zennymorh.movies.roomdb.PopularMovieDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PopularMoviesRepository {
    fun getMovies(): Flow<Result<PagingData<PopularMovieEntity>, AppError>>
}

class PopularMoviesRepositoryImpl @Inject constructor(
    private val movieDao: PopularMovieDao,
    private val apiService: ApiService,
    private val database: PopularMovieDatabase
) : PopularMoviesRepository {

    override fun getMovies(): Flow<Result<PagingData<PopularMovieEntity>, AppError>> = fetchPagedMovies()
        .map { data -> Ok(data) }
        .catch { Err(AppError.UnknownError) }

    @OptIn(ExperimentalPagingApi::class)
    private fun fetchPagedMovies(): Flow<PagingData<PopularMovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = PopularMoviesRemoteMediator(movieDao, apiService, database),
            pagingSourceFactory = { movieDao.getAllMoviesPaged() }
        ).flow
    }
}
