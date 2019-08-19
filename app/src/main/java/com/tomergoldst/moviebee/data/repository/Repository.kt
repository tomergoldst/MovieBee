package com.tomergoldst.moviebee.data.repository

import com.tomergoldst.moviebee.data.local.LocalDataSource
import com.tomergoldst.moviebee.data.remote.RemoteDataSource
import com.tomergoldst.moviebee.models.FavoriteMovie
import com.tomergoldst.moviebee.models.Movie
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class Repository(
    private val moviesLocalDataSource: LocalDataSource,
    private val moviesRemoteDataSource: RemoteDataSource
) : RepositoryDataSource {

    override fun getLatestMovies(page: Int): Observable<List<Movie>> {
        // fetch data from remote and update local database on successful response
        moviesRemoteDataSource.getLatestMovies(page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<Movie>>(){
                override fun onSuccess(movies: List<Movie>) {
                    moviesLocalDataSource.saveMovies(movies)
                        .subscribeOn(Schedulers.io())
                        .doOnError { throwable -> Timber.e(throwable) }
                        .subscribe()                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }
            })

        // always read from local database, when database is updated, the change will be reflected automatically
        // due to using Observable object together with Room
        return moviesLocalDataSource.getMovies(page)
    }

    override fun getMovie(id: Long): Observable<Movie> {
        return moviesLocalDataSource.getMovie(id)
    }

    override fun getMovieSync(id: Long): Movie {
        return moviesLocalDataSource.getMovieSync(id)
    }

    override fun favoriteMovie(id: Long): Completable {
        return moviesLocalDataSource.favoriteMovie(id)
    }

    override fun unFavoriteMovie(id: Long): Completable {
        return moviesLocalDataSource.unFavoriteMovie(id)
    }

    override fun getFavoriteMovies(): Observable<List<FavoriteMovie>> {
        return moviesLocalDataSource.getFavoriteMovies()
    }

    override fun getFavoriteMovie(id: Long): Observable<FavoriteMovie?> {
        return moviesLocalDataSource.getFavoriteMovie(id)
    }

    override fun clear() {
        moviesLocalDataSource.clear()
    }

}
