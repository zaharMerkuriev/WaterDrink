package ru.nsu.merkuriev.waterbalance.presentation.home.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.observe
import ru.nsu.merkuriev.waterbalance.R
import ru.nsu.merkuriev.waterbalance.databinding.ActivityHomeBinding
import ru.nsu.merkuriev.waterbalance.domain.model.NotificationData
import ru.nsu.merkuriev.waterbalance.domain.model.NotificationType
import ru.nsu.merkuriev.waterbalance.presentation.common.view.BaseToolbarActivity
import ru.nsu.merkuriev.waterbalance.presentation.home.viewmodel.HomeViewModel
import ru.nsu.merkuriev.waterbalance.presentation.receiver.WaterBalanceBroadcastReceiver
import ru.nsu.merkuriev.waterbalance.utils.general.MetricsUtils
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.pure.AppNavigator
import java.util.*

class HomeActivity : BaseToolbarActivity<ActivityHomeBinding>() {

    override val navigator: Navigator = AppNavigator(this, 0)

    override fun getLayoutId(): Int = R.layout.activity_home

    override val viewModel: HomeViewModel by viewModels { viewModelFactory }

    private val waterValues = arrayOf(100f, 200f, 400f)

    override fun initToolbar() {
        super.initToolbar()
        setToolbarTitle(R.string.home_screen_title)

        addItem(R.drawable.ic_settings, action = { viewModel.openSettingScreen() })
        addItem(R.drawable.ic_update, action = { showResetWaterBalanceDialog() })
    }

    override fun initUI() {
        super.initUI()

        binding.morningCheckbox.isChecked =
            viewModel.isNotificationTypeEnabled(NotificationType.MORNING)

        binding.fab.setOnClickListener {
            showAddWaterDialog()
        }

        binding.morningTime.setOnClickListener {
            showTimePickerDialog(viewModel.getNotificationData(NotificationType.MORNING))
        }

        binding.morningCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setAlarm(viewModel.getNotificationData(NotificationType.MORNING))
            } else {
                cancelAlarm(NotificationType.MORNING)
            }
            viewModel.setNotificationTypeEnabled(isChecked, NotificationType.MORNING)
        }

        viewModel.getDrinkWaterProportion().observe(this) {
            binding.bottle.setLevel(it)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        viewModel.loadUser()
    }

    private fun showResetWaterBalanceDialog() {
        showActionDialog(
            getString(R.string.home_screen_dialog_reset_title),
            getString(R.string.home_screen_dialog_reset_message),
            positiveAction = { viewModel.resetWaterBalance() }
        )
    }

    private fun showAddWaterDialog() {
        val localizedWaterValues = arrayListOf<String>().apply {
            addAll(waterValues.map {
                MetricsUtils.convertMilliliterToOunceIfNecessary(it).toString()
            })
        }

        AlertDialog.Builder(this)
            .apply {
                setTitle(getString(R.string.home_screen_dialog_add_water))
                setItems(localizedWaterValues.toTypedArray()) { _, which ->
                    viewModel.addDrinkWater(waterValues[which])
                }
            }.show()
    }

    private fun showTimePickerDialog(data: NotificationData) {
        TimePickerDialog(
            this,
            { _, hourOfDay, minuteOfHour ->
                viewModel.setTime(hourOfDay, minuteOfHour, data.notificationType)
                if (viewModel.isNotificationTypeEnabled(data.notificationType)) {
                    setAlarm(viewModel.getNotificationData(data.notificationType))
                }
            },
            data.hour,
            data.minute,
            true
        ).show()
    }

    private fun setAlarm(data: NotificationData) {
        val intent = Intent(this, WaterBalanceBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, data.notificationType.ordinal, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, data.hour)
        calendar.set(Calendar.MINUTE, data.minute)
        calendar.set(Calendar.SECOND, 0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun cancelAlarm(notificationType: NotificationType) {
        val intent = Intent(this, WaterBalanceBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, notificationType.ordinal, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
