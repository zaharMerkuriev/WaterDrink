package ru.nsu.merkuriev.waterbalance.data.repository

import ru.nsu.merkuriev.waterbalance.data.model.DataBaseUser
import ru.nsu.merkuriev.waterbalance.data.source.database.WaterBalanceDataBase
import ru.nsu.merkuriev.waterbalance.domain.model.User

class UserRepository(
    private val dataBase: WaterBalanceDataBase
) {

    fun createUser(user: User) = dataBase.userDao().insert(convertUserToDataBaseUser(user))

    fun updateUser(user: User) = dataBase.userDao().update(convertUserToDataBaseUser(user))

    fun getUser() = dataBase.userDao().getUser(COMMON_USER_ID).map { it.parse() }

    private fun convertUserToDataBaseUser(user: User) =
        DataBaseUser(COMMON_USER_ID, user.name, user.weight, user.activeType)

    companion object {
        private const val COMMON_USER_ID = 1L
    }
}