package ru.nsu.merkuriev.waterbalance.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.nsu.merkuriev.waterbalance.data.repository.SharedPreferencesRepository
import ru.nsu.merkuriev.waterbalance.data.repository.UserRepository
import ru.nsu.merkuriev.waterbalance.data.source.database.WaterBalanceDataBase
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
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSharedPreferencesRepository(sharedPreferences: SharedPreferences) =
        SharedPreferencesRepository(sharedPreferences)

    @Provides
    @Singleton
    fun provideWaterBalanceDataBase(context: Context): WaterBalanceDataBase =
        Room.databaseBuilder(context, WaterBalanceDataBase::class.java, "water_balance_db").build()

    @Provides
    @Singleton
    fun provideUserRepository(dataBase: WaterBalanceDataBase) = UserRepository(dataBase)

    companion object {
        private const val SHARED_PREFERENCES_NAME = "water_balance_prefs"
    }
}