package com.tomergoldst.moviebee.data.local

import com.tomergoldst.moviebee.data.remote.Constants
import com.tomergoldst.moviebee.models.Movie
import com.tomergoldst.moviebee.models.FavoriteMovie
import io.reactivex.Completable
import io.reactivex.Observable

class MoviesLocalDataSource(
    private val movieDao: MovieDao,
    private val favoriteMovieDao: FavoriteMovieDao
) : LocalDataSource {

    override fun getMovies(page: Int): Observable<List<Movie>> {
        return movieDao.getAll((page - 1) * Constants.MOVIES_PER_PAGE, Constants.MOVIES_PER_PAGE)
    }

    override fun saveMovies(movies: List<Movie>): Completable {
        return Completable.fromAction { movieDao.insert(movies) }
    }

    override fun getMovie(id: Long): Observable<Movie> {
        return movieDao.get(id)
    }

    override fun getMovieSync(id: Long): Movie {
        return movieDao.getSync(id)
    }

    override fun getFavoriteMovies(): Observable<List<FavoriteMovie>> {
        return favoriteMovieDao.getAll()
    }

    override fun getFavoriteMoviesSync(page: Int): List<FavoriteMovie> {
        return favoriteMovieDao.getAllSync((page - 1) * Constants.MOVIES_PER_PAGE, Constants.MOVIES_PER_PAGE)
    }

    override fun getFavoriteMovies(page: Int): Observable<List<FavoriteMovie>> {
        return favoriteMovieDao.getAll((page - 1) * Constants.MOVIES_PER_PAGE, Constants.MOVIES_PER_PAGE)
    }

    override fun favoriteMovie(id: Long): Completable {
        return Completable.fromAction { favoriteMovieDao.insert(FavoriteMovie(id, System.currentTimeMillis())) }
    }

    override fun unFavoriteMovie(id: Long): Completable {
        return Completable.fromAction { favoriteMovieDao.delete(id) }
    }

    override fun getFavoriteMovie(id: Long): Observable<FavoriteMovie?> {
        return favoriteMovieDao.get(id)
    }

    override fun clear(): Completable {
        return Completable.fromAction { favoriteMovieDao.deleteAll() }
    }

}