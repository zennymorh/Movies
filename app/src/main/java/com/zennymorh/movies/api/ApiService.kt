package com.zennymorh.movies.api

import com.zennymorh.movies.model.PopularMovies
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(): Result<Response<PopularMovies>>
}