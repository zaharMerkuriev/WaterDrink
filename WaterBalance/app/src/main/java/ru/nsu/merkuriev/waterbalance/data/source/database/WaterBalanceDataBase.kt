package ru.nsu.merkuriev.waterbalance.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nsu.merkuriev.waterbalance.data.converter.ActiveStatusConverter
import ru.nsu.merkuriev.waterbalance.data.model.DataBaseUser

@Database(entities = [DataBaseUser::class], version = 1, exportSchema = false)
@TypeConverters(ActiveStatusConverter::class)
abstract class WaterBalanceDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
}