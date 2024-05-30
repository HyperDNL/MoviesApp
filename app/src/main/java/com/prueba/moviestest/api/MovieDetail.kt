package com.prueba.moviestest.api

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("runtime")
    val runtime: Int,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("overview")
    val overview: String
)

data class Genre(
    @SerializedName("name")
    val name: String
)
