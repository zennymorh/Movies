package com.zennymorh.movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zennymorh.movies.data.PopularMoviesRepository
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.errorhandling.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


@HiltViewModel
open class PopularMoviesViewModel @Inject constructor(
    private val popularMoviesRepository: PopularMoviesRepository
): ViewModel() {

    private val _movies = MutableStateFlow<PagingData<PopularMovieEntity>>(PagingData.empty())
    open val movies: StateFlow<PagingData<PopularMovieEntity>> = _movies

    init {
        viewModelScope.launch {
            popularMoviesRepository.getMovies().collect { result ->
                _movies.value = result.value
            }
        }
    }


    fun refreshPopularMovies() {
        viewModelScope.launch {
            _movies.value = PagingData.empty() // Clear the list to trigger a refresh
        }
    }
}