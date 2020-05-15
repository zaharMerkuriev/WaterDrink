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

    companion object {
        private const val KEY_MORNING_HOUR = "KEY_MORNING_HOUR"
        private const val KEY_MORNING_MINUTE = "KEY_MORNING_MINUTE"
        private const val KEY_MORNING_ENABLED = "KEY_MORNING_ENABLED"

        private const val KEY_DRINK_WATER_VALUE = "KEY_DRINK_WATER_VALUE"
        private const val KEY_FIRST_OPEN = "KEY_FIRST_OPEN"
    }
}