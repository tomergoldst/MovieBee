package com.tomergoldst.moviebee.data.repository

import com.tomergoldst.moviebee.models.FavoriteMovie
import com.tomergoldst.moviebee.models.Movie
import io.reactivex.Completable
import io.reactivex.Observable

interface RepositoryDataSource {

    fun getLatestMovies(page: Int): Observable<List<Movie>>

    fun getMovie(id: Long): Observable<Movie>

    fun getMovieSync(id: Long): Movie

    fun favoriteMovie(id: Long): Completable

    fun unFavoriteMovie(id: Long): Completable

    fun getFavoriteMovies(): Observable<List<FavoriteMovie>>

    fun getFavoriteMovie(id: Long): Observable<FavoriteMovie?>

    fun clear()

}
