package ru.nsu.merkuriev.waterbalance.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
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
        context.getSharedPreferences("water_balance_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideWaterBalanceDataBase(context: Context): WaterBalanceDataBase =
        Room.databaseBuilder(context, WaterBalanceDataBase::class.java, "water_balance_db").build()

    @Provides
    @Singleton
    fun provideUserRepository(dataBase: WaterBalanceDataBase) = UserRepository(dataBase)
}