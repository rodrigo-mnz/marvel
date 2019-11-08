package com.rcaetano.marvelheroes.di

import com.rcaetano.marvelheroes.data.Repository
import com.rcaetano.marvelheroes.data.RepositoryImpl
import com.rcaetano.marvelheroes.feature.detail.DetailViewModel
import com.rcaetano.marvelheroes.feature.home.HomeViewModel
import com.rcaetano.marvelheroes.feature.search.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { DetailViewModel(get()) }

    single<Repository> { RepositoryImpl(get()) }
}
