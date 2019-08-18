package com.tomergoldst.moviebee.app

import androidx.room.Room
import com.tomergoldst.moviebee.data.local.AppDatabase
import com.tomergoldst.moviebee.data.local.LocalDataSource
import com.tomergoldst.moviebee.data.local.MoviesLocalDataSource
import com.tomergoldst.moviebee.data.repository.RepositoryDataSource
import com.tomergoldst.moviebee.data.remote.MoviesRemoteDataSource
import com.tomergoldst.moviebee.data.remote.RemoteDataSource
import com.tomergoldst.moviebee.data.remote.RetrofitClient
import com.tomergoldst.moviebee.data.repository.Repository
import com.tomergoldst.moviebee.ui.FavoriteMoviesViewModel
import com.tomergoldst.moviebee.ui.MainViewModel
import com.tomergoldst.moviebee.ui.MovieDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModules: Module = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().starredMovieDao() }
    single { get<AppDatabase>().movieDao() }
    single<LocalDataSource> { MoviesLocalDataSource(get(), get()) }

    single { RetrofitClient.getMoviesService() }
    single<RemoteDataSource> { MoviesRemoteDataSource(get()) }

    single<RepositoryDataSource> { Repository(get(), get()) }

    viewModel { MainViewModel(get()) }
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { FavoriteMoviesViewModel(get()) }
}

