package com.tomergoldst.moviebee.data.remote

import com.tomergoldst.moviebee.models.Movie
import io.reactivex.Single

interface RemoteDataSource {

    fun getLatestMovies(page: Int) : Single<List<Movie>>

    fun getMovieDetails(id: Long) : Single<Movie>

}
