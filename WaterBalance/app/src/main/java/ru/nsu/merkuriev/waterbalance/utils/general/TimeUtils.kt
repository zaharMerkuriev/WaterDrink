package ru.nsu.merkuriev.waterbalance.utils.general

import java.util.*

object TimeUtils {

    fun formatTimeIntToString24(value: Int) =
        if (value.toString().length == 1) {
            "0$value"
        } else {
            value.toString()
        }

    fun getCalendarByHourAndMinute(hour: Int, minute: Int, toNextDay: Boolean = true): Calendar {
        val calendarTimeToSet = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (toNextDay) {
            val currentTimeCalendar = Calendar.getInstance()
            if (calendarTimeToSet.timeInMillis < currentTimeCalendar.timeInMillis) {
                calendarTimeToSet.add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        return calendarTimeToSet
    }

    fun getMidNightCalendar(toNextDay: Boolean = true): Calendar {
        val calendarTimeToSet = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (toNextDay) {
            val currentTimeCalendar = Calendar.getInstance()
            if (calendarTimeToSet.timeInMillis < currentTimeCalendar.timeInMillis) {
                calendarTimeToSet.add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        return calendarTimeToSet
    }
}