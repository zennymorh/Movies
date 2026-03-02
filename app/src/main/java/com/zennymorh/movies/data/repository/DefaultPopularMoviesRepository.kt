package com.zennymorh.movies.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zennymorh.movies.data.local.PopularMovieDao
import com.zennymorh.movies.data.local.PopularMovieDatabase
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.data.paging.PopularMoviesRemoteMediator
import com.zennymorh.movies.data.remote.api.ApiService
import com.zennymorh.movies.domain.repository.PopularMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPopularMoviesRepository @Inject constructor(
    private val movieDao: PopularMovieDao,
    private val apiService: ApiService,
    private val database: PopularMovieDatabase
) : PopularMoviesRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<PopularMovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            remoteMediator = PopularMoviesRemoteMediator(apiService, database),
            pagingSourceFactory = { movieDao.getAllMoviesPaged() }
        ).flow
    }
}
