package ru.nsu.merkuriev.waterbalance.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.nsu.merkuriev.bodysign.di.ViewModelFactory
import ru.nsu.merkuriev.waterbalance.presentation.create_user.di.CreateUserModule
import ru.nsu.merkuriev.waterbalance.presentation.create_user.view.CreateUserActivity
import ru.nsu.merkuriev.waterbalance.presentation.home.di.HomeModule
import ru.nsu.merkuriev.waterbalance.presentation.home.view.HomeActivity

@Module
abstract class UIModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [CreateUserModule::class])
    abstract fun contributeCreateUserActivity(): CreateUserActivity
}