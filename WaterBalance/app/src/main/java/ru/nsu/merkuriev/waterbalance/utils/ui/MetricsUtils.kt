package ru.nsu.merkuriev.waterbalance.utils.ui

import android.content.res.Resources
import android.os.Build
import java.util.*

object MetricsUtils {

    private const val MILLILITER_IN_OUNCE = 29.5735f
    private val ENGLISH_US_LOCALE = Locale.US

    fun convertOunceToMilliliterIfNecessary(value: Float) =
        if (isEnglishUsLocale(getDeviceLocale())) {
            value * MILLILITER_IN_OUNCE
        } else {
            value
        }


    fun convertMilliliterToOunceIfNecessary(value: Float) =
        if (isEnglishUsLocale(getDeviceLocale())) {
            value / MILLILITER_IN_OUNCE
        } else {
            value
        }


    private fun isEnglishUsLocale(locale: Locale) =
        when (locale) {
            ENGLISH_US_LOCALE -> true
            else -> false
        }


    private fun getDeviceLocale() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0]
        } else {
            Resources.getSystem().configuration.locale
        }
}