package com.tomergoldst.moviebee.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomergoldst.moviebee.models.Movie
import com.tomergoldst.moviebee.models.FavoriteMovie

@Database(
    entities = [FavoriteMovie::class, Movie::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun starredMovieDao(): FavoriteMovieDao
    abstract fun movieDao(): MovieDao

    companion object {

        const val DATABASE_NAME = "com.tomergoldst.moviebee.db"

    }

}
