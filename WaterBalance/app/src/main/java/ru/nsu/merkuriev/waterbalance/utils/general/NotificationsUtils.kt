package ru.nsu.merkuriev.waterbalance.utils.general

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context

object NotificationsUtils {
    fun setAlarm(context: Context, intent: PendingIntent, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(intent)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            TimeUtils.getCalendarByHourAndMinute(hour, minute).timeInMillis,
            AlarmManager.INTERVAL_DAY,
            intent
        )
    }

    fun cancelAlarm(context: Context, intent: PendingIntent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(intent)
    }
}