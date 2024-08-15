package com.zennymorh.movies.model

import com.google.gson.annotations.SerializedName

data class PopularMovies(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
data class Movie(
    val id: String,
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("original_language")
    val originalLanguage: String,
)