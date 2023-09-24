package com.rasel.moviedb.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Immutable model class for a Github repo that holds all the information about a repository.
 * Objects of this type are received from the Github API, therefore all the fields are annotated
 * with the serialized name.
 * This class also defines the Room repos table, where the repo [id] is the primary key.
 */
@Entity(tableName = "repos")
data class MovieInfo(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
   /* @SerializedName("genre_ids")
    val genreIds: List<Int>,*/
    @SerializedName("id")
    @PrimaryKey val id: Int,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,

    val isFavorite : Boolean = false
)
