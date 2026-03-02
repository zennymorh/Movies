package com.zennymorh.movies.domain.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zennymorh.movies.data.remote.api.ApiService
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.data.paging.PopularMoviesRemoteMediator
import com.zennymorh.movies.data.local.PopularMovieDao
import com.zennymorh.movies.data.local.PopularMovieDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PopularMoviesRepository {
    fun getMovies(): Flow<PagingData<PopularMovieEntity>>
}