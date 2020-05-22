package ru.nsu.merkuriev.waterbalance.presentation.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.nsu.merkuriev.waterbalance.R
import ru.nsu.merkuriev.waterbalance.presentation.home.viewmodel.HomeViewModel
import ru.nsu.merkuriev.waterbalance.utils.general.MetricsUtils
import java.security.SecureRandom


class WaterBalanceNotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
    }

    private fun showNotification(context: Context) {

        val manager =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.water_balance_channel)
        ).apply {
            setContentTitle(context.getString(R.string.notification_title))
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                priority = Notification.PRIORITY_HIGH
            } else {
                manager.createNotificationChannel(
                    NotificationChannel(
                        context.getString(R.string.water_balance_channel),
                        context.getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )
            }
            setSmallIcon(R.mipmap.ic_launcher)
            setDefaults(Notification.DEFAULT_SOUND)
            setAutoCancel(true)
            setContentText(context.getString(R.string.home_screen_dialog_add_water))
            addAction(
                R.drawable.ic_plus,
                MetricsUtils.convertMilliliterToOunceIfNecessary(HomeViewModel.waterValuesToAdd[0])
                    .toString(),
                getAddWaterIntent(context, HomeViewModel.waterValuesToAdd[0])
            )
            addAction(
                R.drawable.ic_plus,
                MetricsUtils.convertMilliliterToOunceIfNecessary(HomeViewModel.waterValuesToAdd[1])
                    .toString(),
                getAddWaterIntent(context, HomeViewModel.waterValuesToAdd[1])
            )
            addAction(
                R.drawable.ic_plus,
                MetricsUtils.convertMilliliterToOunceIfNecessary(HomeViewModel.waterValuesToAdd[2])
                    .toString(),
                getAddWaterIntent(context, HomeViewModel.waterValuesToAdd[2])
            )
        }

        manager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun getAddWaterIntent(context: Context, value: Float): PendingIntent {
        val intent = AddWaterBroadcastReceiver.getIntent(context, value)

        return PendingIntent.getBroadcast(
            context,
            SecureRandom().nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    companion object {

        private const val NOTIFICATION_ID = 555

        fun getIntent(context: Context) =
            Intent(context, WaterBalanceNotificationBroadcastReceiver::class.java)

        fun cancelNotification(context: Context) {
            val manager =
                (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

            manager.cancel(NOTIFICATION_ID)
        }
    }
}