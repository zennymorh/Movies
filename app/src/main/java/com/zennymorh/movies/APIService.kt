package com.zennymorh.movies

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY = "d5c2a0fd5dc0cb6f0b41de11ec78d19b"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface MovieAPIService {
    @GET("movie/top_rated?api_key=$API_KEY")
    suspend fun getTopRatedMovies():
            ArrayList<MovieResult>

    @GET("movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovies():
            MovieResult

    @GET("movie/{id}/videos?api_key=$API_KEY")
    suspend fun getMovieTrailers():
            ArrayList<TrailerResult>

    @GET("movie/{id}/reviews?api_key=$API_KEY")
    suspend fun getReviews(@Path("id") id: String):
            ArrayList<ReviewResult>
}

object APIService {
    val retrofitService : MovieAPIService by lazy {
        retrofit.create(MovieAPIService::class.java)
    }
}