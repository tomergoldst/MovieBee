package com.tomergoldst.moviebee.data.local

import androidx.room.*
import com.tomergoldst.moviebee.models.Movie
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY release_date DESC LIMIT :limit OFFSET :offset")
    fun getAll(offset: Int, limit: Int): Observable<List<Movie>>

    @Query("SELECT * FROM movie ORDER BY release_date DESC LIMIT :limit OFFSET :offset")
    fun getAllSync(offset: Int, limit: Int): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun get(id: Long): Observable<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getSync(id: Long): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<Movie>)

    @Update
    fun update(movie: Movie)

    @Delete
    fun delete(movie: Movie)

    @Query("DELETE FROM movie")
    fun deleteAll()

}
