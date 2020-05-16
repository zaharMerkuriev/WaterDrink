package ru.nsu.merkuriev.waterbalance.presentation.receiver

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import ru.nsu.merkuriev.waterbalance.data.repository.SharedPreferencesRepository
import javax.inject.Inject


class ResetWaterBalanceBroadcastReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var preferencesRepository: SharedPreferencesRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        resetWaterBalance()
    }

    private fun resetWaterBalance() {
        preferencesRepository.setDrinkWaterValue(0f)
    }

    companion object {

        fun getIntent(context: Context) =
            Intent(context, ResetWaterBalanceBroadcastReceiver::class.java)
    }
}