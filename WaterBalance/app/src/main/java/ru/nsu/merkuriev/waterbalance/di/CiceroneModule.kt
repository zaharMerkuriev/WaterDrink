package ru.nsu.merkuriev.waterbalance.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class CiceroneModule {

    @Provides
    @Singleton
    fun provideBaseCicerone(): Cicerone<Router> = Cicerone.create(Router())

    @Provides
    @Singleton
    fun provideBaseRouter(cicerone: Cicerone<Router>): Router = cicerone.router

    @Provides
    @Singleton
    fun provideBaseNavigationHolder(cicerone: Cicerone<Router>): NavigatorHolder =
        cicerone.navigatorHolder

}