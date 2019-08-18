package com.tomergoldst.moviebee.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_movie")
data class FavoriteMovie(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var movieId: Long,

    @SerializedName("timestamp")
    @ColumnInfo(name = "timestamp")
    var timestamp: Long

)