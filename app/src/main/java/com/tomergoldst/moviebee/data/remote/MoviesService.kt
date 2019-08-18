package com.tomergoldst.moviebee.data.remote

import com.tomergoldst.moviebee.models.DiscoverMoviesResponse
import com.tomergoldst.moviebee.models.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MoviesService {

    @GET("discover/movie")
    fun discoverMovies(@QueryMap params: Map<String, String>): Single<DiscoverMoviesResponse>

    @GET("/movie/{id}")
    fun getMovieDetails(@Path("id") id: Long, @QueryMap params: Map<String, String>): Single<Movie>

}