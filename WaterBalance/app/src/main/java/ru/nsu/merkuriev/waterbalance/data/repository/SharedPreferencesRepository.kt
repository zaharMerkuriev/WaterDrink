package ru.nsu.merkuriev.waterbalance.data.repository

import android.content.SharedPreferences

class SharedPreferencesRepository(
    private val prefs: SharedPreferences
) {

    fun isAppFirstOpen() = prefs.getBoolean(KEY_FIRST_OPEN, true)

    fun setIsAppFirstOpen(value: Boolean) = prefs.edit().putBoolean(KEY_FIRST_OPEN, value).apply()

    fun getDrinkWaterValue() = prefs.getFloat(KEY_DRINK_WATER_VALUE, 0f)

    fun setDrinkWaterValue(value: Float) =
        prefs.edit().putFloat(KEY_DRINK_WATER_VALUE, value).apply()

    fun getMorningHour() = prefs.getInt(KEY_MORNING_HOUR, 0)

    fun setMorningHour(value: Int) = prefs.edit().putInt(KEY_MORNING_HOUR, value).apply()

    fun getMorningMinute() = prefs.getInt(KEY_MORNING_MINUTE, 0)

    fun setMorningMinute(value: Int) = prefs.edit().putInt(KEY_MORNING_MINUTE, value).apply()

    fun isMorningAlarmEnabled() = prefs.getBoolean(KEY_MORNING_ENABLED, false)

    fun setMorningEnabled(value: Boolean) =
        prefs.edit().putBoolean(KEY_MORNING_ENABLED, value).apply()

    fun getDayHour() = prefs.getInt(KEY_DAY_HOUR, 0)

    fun setDayHour(value: Int) = prefs.edit().putInt(KEY_DAY_HOUR, value).apply()

    fun getDayMinute() = prefs.getInt(KEY_DAY_MINUTE, 0)

    fun setDayMinute(value: Int) = prefs.edit().putInt(KEY_DAY_MINUTE, value).apply()

    fun isDayAlarmEnabled() = prefs.getBoolean(KEY_DAY_ENABLED, false)

    fun setDayEnabled(value: Boolean) =
        prefs.edit().putBoolean(KEY_DAY_ENABLED, value).apply()

    fun getEveningHour() = prefs.getInt(KEY_EVENING_HOUR, 0)

    fun setEveningHour(value: Int) = prefs.edit().putInt(KEY_EVENING_HOUR, value).apply()

    fun getEveningMinute() = prefs.getInt(KEY_EVENING_MINUTE, 0)

    fun setEveningMinute(value: Int) = prefs.edit().putInt(KEY_EVENING_MINUTE, value).apply()

    fun isEveningAlarmEnabled() = prefs.getBoolean(KEY_EVENING_ENABLED, false)

    fun setEveningEnabled(value: Boolean) =
        prefs.edit().putBoolean(KEY_EVENING_ENABLED, value).apply()

    companion object {
        private const val KEY_MORNING_HOUR = "KEY_MORNING_HOUR"
        private const val KEY_MORNING_MINUTE = "KEY_MORNING_MINUTE"
        private const val KEY_MORNING_ENABLED = "KEY_MORNING_ENABLED"

        private const val KEY_DAY_HOUR = "KEY_DAY_HOUR"
        private const val KEY_DAY_MINUTE = "KEY_DAY_MINUTE"
        private const val KEY_DAY_ENABLED = "KEY_DAY_ENABLED"

        private const val KEY_EVENING_HOUR = "KEY_EVENING_HOUR"
        private const val KEY_EVENING_MINUTE = "KEY_EVENING_MINUTE"
        private const val KEY_EVENING_ENABLED = "KEY_EVENING_ENABLED"

        private const val KEY_DRINK_WATER_VALUE = "KEY_DRINK_WATER_VALUE"
        private const val KEY_FIRST_OPEN = "KEY_FIRST_OPEN"
    }
}