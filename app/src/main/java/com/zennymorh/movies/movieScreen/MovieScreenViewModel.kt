package com.zennymorh.movies.movieScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zennymorh.movies.APIService
import com.zennymorh.movies.Movie
import kotlinx.coroutines.launch

class MovieScreenViewModel: ViewModel() {
    private val _movies = MutableLiveData<ArrayList<Movie>>()
    val movies: LiveData<ArrayList<Movie>>
        get() = _movies

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    init {
        getMovieList()
    }

    private fun getMovieList() {
        viewModelScope.launch {
            try {
                val listMovies = APIService.retrofitService.getPopularMovies()
                _movies.value= listMovies.results
            } catch (t: Throwable) {
                Log.i("MSVModel", t.localizedMessage!!)
            }
        }
    }
}