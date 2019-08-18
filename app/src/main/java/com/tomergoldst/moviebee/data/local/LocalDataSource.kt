package com.tomergoldst.moviebee.data.local

import com.tomergoldst.moviebee.models.Movie
import com.tomergoldst.moviebee.models.FavoriteMovie
import io.reactivex.Completable
import io.reactivex.Observable

interface LocalDataSource {

    fun getMovies(page: Int) : Observable<List<Movie>>

    fun saveMovies(movies: List<Movie>): Completable

    fun getMovie(id: Long) : Observable<Movie>

    fun getMovieSync(id: Long) : Movie

    fun getFavoriteMovies() : Observable<List<FavoriteMovie>>

    fun getFavoriteMovies(page: Int) : Observable<List<FavoriteMovie>>

    fun getFavoriteMoviesSync(page: Int) : List<FavoriteMovie>

    fun getFavoriteMovie(id: Long) : Observable<FavoriteMovie?>

    fun favoriteMovie(id: Long): Completable

    fun unFavoriteMovie(id: Long): Completable

    fun clear(): Completable

}
