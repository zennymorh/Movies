package com.zennymorh.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.paging.PopularMoviesRemoteMediator
import com.zennymorh.movies.roomdb.PopularMovieDao
import com.zennymorh.movies.roomdb.PopularMovieDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PopularMoviesRepository {
    fun getMovies(): Flow<PagingData<PopularMovieEntity>>
}

class PopularMoviesRepositoryImpl @Inject constructor(
    private val movieDao: PopularMovieDao,
    private val apiService: ApiService,
    private val database: PopularMovieDatabase
) : PopularMoviesRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<PopularMovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = PopularMoviesRemoteMediator(movieDao, apiService, database),
            pagingSourceFactory = { movieDao.getAllMoviesPaged() }
        ).flow
    }
}
