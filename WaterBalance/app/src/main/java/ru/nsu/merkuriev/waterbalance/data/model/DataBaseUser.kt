package ru.nsu.merkuriev.waterbalance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nsu.merkuriev.waterbalance.domain.model.ActiveType
import ru.nsu.merkuriev.waterbalance.domain.model.User

@Entity(tableName = "user_table")
data class DataBaseUser(
    @PrimaryKey
    val id: Long,
    val name: String,
    val weight: Float,
    val activeType: ActiveType
) : Convertable<User> {
    override fun parse(): User = User(name, weight, activeType)
}