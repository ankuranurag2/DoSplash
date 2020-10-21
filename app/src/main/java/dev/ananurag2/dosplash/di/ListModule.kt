package dev.ananurag2.dosplash.di

import dev.ananurag2.dosplash.data.network.ApiService
import dev.ananurag2.dosplash.repository.ImageRepository
import dev.ananurag2.dosplash.ui.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * created by ankur on 21/10/20
 */
val listModule = module {
    viewModel { ListViewModel(get()) }
    single { getImageRepo(get()) }
}

fun getImageRepo(apiService: ApiService) = ImageRepository(apiService)