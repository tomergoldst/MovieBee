package com.tomergoldst.moviebee.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomergoldst.moviebee.data.repository.RepositoryDataSource
import com.tomergoldst.moviebee.models.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import kotlin.collections.ArrayList

class FavoriteMoviesViewModel(
    private val repository: RepositoryDataSource
) :
    ViewModel() {

    private val _movies: MutableLiveData<List<Movie>> = MutableLiveData()
    val movies: LiveData<List<Movie>> = _movies

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val mCompositeDisposable = CompositeDisposable()

    init {
        _movies.value = ArrayList()
        _dataLoading.value = true
        _error.value = false
        subscribeForData()
    }

    private fun subscribeForData() {
        mCompositeDisposable.add(
            repository.getFavoriteMovies()
                .observeOn(Schedulers.io())
                .map { list -> list.map { repository.getMovieSync(it.movieId) }.toList() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateData, this::onError)
        )
    }

    private fun updateData(movies: List<Movie>) {
        Timber.d("updateData")

        _movies.value = movies

        if (_dataLoading.value == true) {
            _dataLoading.value = false
        }
    }

    private fun onError(e: Throwable) {
        Timber.e(e)
        _dataLoading.value = false
        _error.value = true
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }

}