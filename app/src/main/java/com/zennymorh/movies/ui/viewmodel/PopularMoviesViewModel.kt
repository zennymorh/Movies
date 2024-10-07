package com.zennymorh.movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zennymorh.movies.data.PopularMoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val popularMoviesRepository: PopularMoviesRepository
): ViewModel() {
    private val _userName = MutableStateFlow("test")
    val userName: StateFlow<String> = _userName

    suspend fun display() {
        viewModelScope.launch {
//            popularMoviesRepository.getPopularMovies()
            _userName.value = "John Doe"
        }
    }
}