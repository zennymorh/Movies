package com.zennymorh.movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zennymorh.movies.domain.repository.PopularMoviesRepository
import com.zennymorh.movies.data.model.PopularMovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    popularMoviesRepository: PopularMoviesRepository
) : ViewModel() {

    val movies: Flow<PagingData<PopularMovieEntity>> =
        popularMoviesRepository
            .getMovies()
            .cachedIn(viewModelScope)
}
