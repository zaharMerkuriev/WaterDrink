package ru.nsu.merkuriev.waterbalance.domain.model

data class User(
    val name: String,
    val weight: Float,
    val activeType: ActiveType
)