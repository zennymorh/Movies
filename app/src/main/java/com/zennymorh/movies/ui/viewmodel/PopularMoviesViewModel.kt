package com.zennymorh.movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Err
import com.zennymorh.movies.data.PopularMoviesRepository
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.errorhandling.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.github.michaelbull.result.Result
import javax.inject.Inject


@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val popularMoviesRepository: PopularMoviesRepository
): ViewModel() {

    private val _movies = MutableStateFlow<Result<List<PopularMovieEntity>, AppError>>(Err(AppError.UnknownError))
    val movies: StateFlow<Result<List<PopularMovieEntity>, AppError>> = _movies

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            popularMoviesRepository.getMovies().collect { result ->
                _movies.value = result
            }
        }
    }

    fun refreshPopularMovies() {
        viewModelScope.launch {
            _movies.value = popularMoviesRepository.fetchAndCacheMovies()
        }
    }
}