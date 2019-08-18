package com.tomergoldst.moviebee.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomergoldst.moviebee.data.repository.RepositoryDataSource
import com.tomergoldst.moviebee.models.FavoriteMovie
import com.tomergoldst.moviebee.models.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MovieDetailsViewModel(
    private val repository: RepositoryDataSource
) :
    ViewModel() {

    private val _movie: MutableLiveData<Movie?> = MutableLiveData()
    val movie: LiveData<Movie?> = _movie

    private val _isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val mCompositeDisposable = CompositeDisposable()

    init {
        _isFavorite.value = false
    }

    fun init(id: Long) {
        mCompositeDisposable.add(
            repository.getMovie(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateData, this::onError)
        )

        mCompositeDisposable.add(
            repository.getFavoriteMovie(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateFavoriteMarker, this::onError)
        )
    }

    private fun updateData(movie: Movie) {
        _movie.value = movie
    }

    private fun updateFavoriteMarker(favoriteMovie: FavoriteMovie?) {
        _isFavorite.value = favoriteMovie != null
    }

    private fun onError(e: Throwable) {
        Timber.e(e)
    }

    fun toggleFavoriteMovie() {
        if (isFavorite.value!!) {
            mCompositeDisposable.add(
                repository.unFavoriteMovie(_movie.value!!.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        // Room has a bug, when delete row from db it doesn't update the query observing this
                        // row with null value
                        // Hack this by manually calling update favorite marker with null value upon completion
                        updateFavoriteMarker(null)
                    })
        } else {
            mCompositeDisposable.add(
                repository.favoriteMovie(_movie.value!!.id)
                    .subscribeOn(Schedulers.computation())
                    .subscribe()
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }

}