package ru.nsu.merkuriev.waterbalance.presentation.home.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nsu.merkuriev.bodysign.di.ViewModelKey
import ru.nsu.merkuriev.waterbalance.di.ActivityScope
import ru.nsu.merkuriev.waterbalance.presentation.home.viewmodel.HomeViewModel

@Module
abstract class HomeModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel

}