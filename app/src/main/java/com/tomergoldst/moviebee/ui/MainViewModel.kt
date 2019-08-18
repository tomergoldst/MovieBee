package com.tomergoldst.moviebee.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomergoldst.moviebee.data.repository.RepositoryDataSource
import com.tomergoldst.moviebee.models.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class MainViewModel(
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

    private val mCompositeDisposable = CompositeDisposable()
    private val mPagination: PublishSubject<Int> = PublishSubject.create()

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
                .doOnNext { page -> Timber.d("page $page") }
                .flatMap { page -> repository.getLatestMovies(page) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateData, this::onError)
        )

        mPagination.onNext(mPage)

    }

    private fun updateData(movies: List<Movie>){
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

    private fun onError(e: Throwable){
        Timber.e(e)
        _dataLoading.value = false
        _error.value = true
    }

    private fun getMoviesListFromMap(): List<Movie> {
        val movies = ArrayList<Movie>()
        for (moviesList in mMoviesMap) {
            movies.addAll(moviesList.value)
        }
        return movies
    }

    fun getMoreMovies() {
        mPagination.onNext(++mPage)
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }
}