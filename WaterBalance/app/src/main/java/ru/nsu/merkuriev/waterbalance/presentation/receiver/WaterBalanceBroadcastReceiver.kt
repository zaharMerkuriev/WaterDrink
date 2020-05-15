package ru.nsu.merkuriev.waterbalance.presentation.receiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.nsu.merkuriev.waterbalance.R
import ru.nsu.merkuriev.waterbalance.presentation.create_user.view.CreateUserActivity
import java.security.SecureRandom


class WaterBalanceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, CreateUserActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val manager =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.water_balance_channel)
        ).apply {
            setContentTitle(context.getString(R.string.notification_title))
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                priority = Notification.PRIORITY_HIGH
            }
            setSmallIcon(R.mipmap.ic_launcher)
            setContentIntent(activityPendingIntent)
            setDefaults(Notification.DEFAULT_SOUND)
            setAutoCancel(true)
        }

        manager.notify(SecureRandom().nextInt(), builder.build())
    }
}