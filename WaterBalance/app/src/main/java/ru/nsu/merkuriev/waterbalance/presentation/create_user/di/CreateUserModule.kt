package ru.nsu.merkuriev.waterbalance.presentation.create_user.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nsu.merkuriev.bodysign.di.ViewModelKey
import ru.nsu.merkuriev.waterbalance.di.ActivityScope
import ru.nsu.merkuriev.waterbalance.presentation.create_user.viewmodel.CreateUserViewModel

@Module
abstract class CreateUserModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(CreateUserViewModel::class)
    abstract fun bindViewModel(viewModel: CreateUserViewModel): ViewModel
}