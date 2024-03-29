package com.tomergoldst.moviebee.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "movie")
data class Movie(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Long,

    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count")
    var voteCount: Long,

    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    var voteAverage: Float = 0f,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String = "",

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    var posterPath: String? = "",

    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String? = "",

    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    var overview: String = "",

    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    var releaseDate: Date,

    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    var popularity: Float,

    @SerializedName("page")
    @ColumnInfo(name = "page")
    var page: Int

)