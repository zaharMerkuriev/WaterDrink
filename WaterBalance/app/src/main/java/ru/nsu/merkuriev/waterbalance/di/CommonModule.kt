package ru.nsu.merkuriev.waterbalance.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.nsu.merkuriev.waterbalance.utils.rx.RxSchedulers
import javax.inject.Singleton


@Module
class CommonModule {

    @Provides
    @Singleton
    fun provideRxSchedulers(): RxSchedulers = RxSchedulers()

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) =
        context.getSharedPreferences("water_balance_prefs", Context.MODE_PRIVATE)
}