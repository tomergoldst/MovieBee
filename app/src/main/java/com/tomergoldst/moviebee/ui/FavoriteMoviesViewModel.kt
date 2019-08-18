package com.tomergoldst.moviebee.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomergoldst.moviebee.data.repository.RepositoryDataSource
import com.tomergoldst.moviebee.models.FavoriteMovie
import com.tomergoldst.moviebee.models.Movie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

// Todo: handle case where favorite movie is removed from favorites while we still live
class FavoriteMoviesViewModel(
    private val repository: RepositoryDataSource
) :
    ViewModel() {

    private val mMoviesMap: MutableMap<Int, List<Movie>> = LinkedHashMap()

    private val _movies: MutableLiveData<List<Movie>> = MutableLiveData()
    val movies: LiveData<List<Movie>> = _movies

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private var mPage = 1

    private val mPagination: PublishSubject<Int> = PublishSubject.create()
    private val mCompositeDisposable = CompositeDisposable()

    init {
        _movies.value = ArrayList()
        _dataLoading.value = true
        _error.value = false
        subscribeForData()
    }

    private fun subscribeForData() {
        Timber.d("subscribeForData")

        mCompositeDisposable.add(
            mPagination
                .observeOn(Schedulers.io())
                .map { page -> repository.getFavoriteMoviesSync(page) }
                .doOnNext{ list -> Timber.d("1 $list")}
                .flatMap { list -> Observable.fromCallable { getListOfMoviesFromListOfFavorites(list) } }
                .doOnNext{ list -> Timber.d("2 $list")}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateData, this::onError)
        )

        mPagination.onNext(mPage)

    }

    private fun getListOfMoviesFromListOfFavorites(list: List<FavoriteMovie>): List<Movie> {
        val movies: MutableList<Movie> = ArrayList()
        for (fm in list) {
            val m = repository.getMovieSync(fm.movieId)
            movies.add(m)
        }
        return movies.toList()
    }

    private fun updateData(movies: List<Movie>) {
        Timber.d("updateData")

        if (!movies.isNullOrEmpty()) {
            mMoviesMap[mPage] = movies
            _movies.value = getMoviesListFromMap()

        } else {
            Timber.d("updateData got empty list")
        }

        if (_dataLoading.value == true) {
            _dataLoading.value = false
        }
    }

    private fun getMoviesListFromMap(): List<Movie> {
        val movies = ArrayList<Movie>()
        for (moviesList in mMoviesMap) {
            movies.addAll(moviesList.value)
        }
        return movies
    }

    private fun onError(e: Throwable) {
        Timber.e(e)
        _dataLoading.value = false
        _error.value = true
    }

    fun getMoreMovies() {
        mPagination.onNext(++mPage)
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }

}