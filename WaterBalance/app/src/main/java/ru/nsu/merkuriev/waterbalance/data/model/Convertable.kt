package ru.nsu.merkuriev.waterbalance.data.model

interface Convertable<T> {
    fun parse(): T
}