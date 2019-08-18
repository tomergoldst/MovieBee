package com.tomergoldst.moviebee.data.local

import androidx.room.*
import com.tomergoldst.moviebee.models.FavoriteMovie
import io.reactivex.Observable

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movie ORDER BY timestamp")
    fun getAll(): Observable<List<FavoriteMovie>>

    @Query("SELECT * FROM favorite_movie ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getAll(offset: Int, limit: Int): Observable<List<FavoriteMovie>>

    @Query("SELECT * FROM favorite_movie ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getAllSync(offset: Int, limit: Int): List<FavoriteMovie>

    @Query("SELECT * FROM favorite_movie WHERE id = :movieId ")
    fun get(movieId: Long): Observable<FavoriteMovie?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteMovie: FavoriteMovie)

    @Query("DELETE From favorite_movie WHERE id = :movieId")
    fun delete(movieId: Long)

    @Query("DELETE FROM favorite_movie")
    fun deleteAll()

}
