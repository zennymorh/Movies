package com.zennymorh.movies.api

import com.zennymorh.movies.data.model.PopularMovies
import retrofit2.http.GET

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(): PopularMovies
}