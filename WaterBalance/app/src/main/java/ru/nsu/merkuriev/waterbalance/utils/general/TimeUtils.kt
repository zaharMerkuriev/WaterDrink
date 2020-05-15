package ru.nsu.merkuriev.waterbalance.utils.general

object TimeUtils {

    fun formatTimeIntToString24(value: Int) =
        if (value.toString().length == 1) {
            "0$value"
        } else {
            value.toString()
        }
}