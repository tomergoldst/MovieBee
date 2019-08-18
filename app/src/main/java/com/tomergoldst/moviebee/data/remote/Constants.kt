package com.tomergoldst.moviebee.data.remote

object Constants {

    const val API_KEY_THE_MOVIE_DB = "91e6219a342912805a925299e835af28"

    const val PARAM_API_KEY = "api_key"
    const val PARAM_PAGE = "page"
    const val PARAM_LANGUAGE = "language"
    const val PARAM_INCLUDE_ADULT = "include_adult"
    const val PARAM_INCLUDE_VIDEO = "include_video"
    const val PARAM_SORT_BY = "sort_by"
    const val PARAM_RELEASE_DATE_LTE = "release_date.lte"
    const val PARAM_PRIMARY_RELEASE_DATE_LTE = "primary_release_date.lte"
    const val PARAM_PRIMARY_RELEASE_DATE_GTE = "primary_release_date.gte"

    const val SORT_BY_POPULARITY_DESC = "popularity.desc"
    const val SORT_BY_RELEASE_DATE_DESC = "release_date.desc"
    const val SORT_BY_PRIMARY_RELEASE_DATE_DESC = "primary_release_date.desc"

    const val MOVIES_PER_PAGE = 20
    const val POSTER_WIDTH = 300
    const val POSTER_PATH = "https://image.tmdb.org/t/p/w%s/%s"

}