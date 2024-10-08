package com.zennymorh.movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zennymorh.movies.data.datasource.remote.RemoteMovieRepository
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val remoteMovieRepository: RemoteMovieRepository
): ViewModel() {
    private val _userName = MutableStateFlow("test")
    val userName: StateFlow<String> = _userName

    suspend fun display() {
        viewModelScope.launch {
//            movieRepository.getPopularMovies()
            _userName.value = "John Doe"
        }
    }
}