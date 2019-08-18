package com.tomergoldst.moviebee.data.remote

import com.tomergoldst.moviebee.models.Movie
import io.reactivex.Single
import java.util.*

class MoviesRemoteDataSource(private val moviesService: MoviesService) :
    RemoteDataSource {

    private val calendar = Calendar.getInstance()
    private val calendarRemoteFormat =
        "${calendar.get(Calendar.YEAR)}-" +
                "${String.format("%02d", calendar.get(Calendar.MONTH) + 1)}-" +
                String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))

    override fun getLatestMovies(page: Int): Single<List<Movie>> {
        val queryParams = getBaseQueryParams()
        queryParams[Constants.PARAM_INCLUDE_ADULT] = false.toString()
        queryParams[Constants.PARAM_INCLUDE_VIDEO] = false.toString()
        queryParams[Constants.PARAM_PAGE] = page.toString()
        queryParams[Constants.PARAM_SORT_BY] = Constants.SORT_BY_PRIMARY_RELEASE_DATE_DESC
        queryParams[Constants.PARAM_PRIMARY_RELEASE_DATE_LTE] = calendarRemoteFormat

        return moviesService.discoverMovies(queryParams)
            .map { r -> r.results }
    }

    override fun getMovieDetails(id: Long): Single<Movie> {
        return moviesService.getMovieDetails(id, getBaseQueryParams())
    }

    private fun getBaseQueryParams(): MutableMap<String, String> {
        val queryParams: MutableMap<String, String> = HashMap()
        queryParams[Constants.PARAM_API_KEY] = Constants.API_KEY_THE_MOVIE_DB
        queryParams[Constants.PARAM_LANGUAGE] = Locale.US.toLanguageTag()
        return queryParams
    }

}