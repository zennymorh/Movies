package com.zennymorh.movies.api

import com.zennymorh.movies.data.model.PopularMovies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): Response<PopularMovies>
}
