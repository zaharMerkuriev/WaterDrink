package ru.nsu.merkuriev.waterbalance.presentation.settings.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nsu.merkuriev.bodysign.di.ViewModelKey
import ru.nsu.merkuriev.waterbalance.di.ActivityScope
import ru.nsu.merkuriev.waterbalance.presentation.settings.viewmodel.SettingsViewModel

@Module
abstract class SettingsModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindViewModel(viewModel: SettingsViewModel): ViewModel
}