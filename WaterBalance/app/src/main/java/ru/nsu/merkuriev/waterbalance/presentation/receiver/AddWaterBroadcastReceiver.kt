package ru.nsu.merkuriev.waterbalance.presentation.receiver

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import ru.nsu.merkuriev.waterbalance.data.repository.SharedPreferencesRepository
import javax.inject.Inject


class AddWaterBroadcastReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var preferencesRepository: SharedPreferencesRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        addWater(intent)

        WaterBalanceNotificationBroadcastReceiver.cancelNotification(context)
    }

    private fun addWater(intent: Intent) {
        val valueToAdd = intent.getFloatExtra(KEY_WATER_VALUE_TO_ADD, 0f)
        preferencesRepository.setDrinkWaterValue(preferencesRepository.getDrinkWaterValue() + valueToAdd)
    }

    companion object {

        fun getIntent(context: Context, value: Float) =
            Intent(context, AddWaterBroadcastReceiver::class.java).apply {
                putExtra(KEY_WATER_VALUE_TO_ADD, value)
            }

        private const val KEY_WATER_VALUE_TO_ADD = "KEY_WATER_VALUE_TO_ADD"
    }
}