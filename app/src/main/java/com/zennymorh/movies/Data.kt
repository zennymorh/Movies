package com.zennymorh.movies

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MovieResult(
    val page: Int,
    val results: ArrayList<Movie>,
    @SerializedName("total_results")
    val totalResult: Int,
    @SerializedName("total_pages")
    val totalPages: Int
): Parcelable

@Parcelize
data class TrailerResult(
    val id: Int,
    val results: ArrayList<Trailer>
): Parcelable

@Parcelize
data class ReviewResult(
    val id: Int,
    val page: Int,
    val results: ArrayList<Review>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults:Int
): Parcelable

@Parcelize
data class Movie(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    val adult: Boolean,
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    val backdropPath: String
): Parcelable

@Parcelize
data class Trailer(
    val id: String,
    val iso_639_1: String,
    val iso_3166_1: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
): Parcelable

@Parcelize
data class Review(
    val id: String,
    val author: String,
    val content: String,
    val url: String
): Parcelable
