package ru.nsu.merkuriev.waterbalance.data.source.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Single
import ru.nsu.merkuriev.waterbalance.data.model.DataBaseUser


@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUser(id: Long): Single<DataBaseUser>

    @Insert
    fun insert(employee: DataBaseUser): Completable

    @Update
    fun update(employee: DataBaseUser): Completable
}