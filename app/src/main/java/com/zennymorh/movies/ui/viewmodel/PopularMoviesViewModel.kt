package com.zennymorh.movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zennymorh.movies.data.PopularMoviesRepository
import com.zennymorh.movies.data.model.Movie
import com.zennymorh.movies.errorhandling.ApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.zennymorh.movies.errorhandling.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val popularMoviesRepository: PopularMoviesRepository
): ViewModel() {
    // UI state holding the result (Success, Error, Loading)
    private val _moviesState = MutableStateFlow<Result<List<Movie>, ApiError>>(Result.Loading)
    val moviesState: StateFlow<Result<List<Movie>, ApiError>> = _moviesState

    // Function to fetch popular movies
    fun fetchPopularMovies() {
        viewModelScope.launch {
            popularMoviesRepository.getPopularMoviesList()
                .onStart {
                    _moviesState.value = Result.Loading // Emit loading state
                }
                .catch { e ->
                    // Handle any exception thrown by the flow
                    _moviesState.value = Result.Error(ApiError(-1, e.message ?: "Unknown Error"))
                }
                .collect { result ->
                    // Collect the result and update the state
                    _moviesState.value = result
                }
        }
    }
}