package ru.nsu.merkuriev.waterbalance.presentation.home.view

import android.app.PendingIntent
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.observe
import ru.nsu.merkuriev.waterbalance.R
import ru.nsu.merkuriev.waterbalance.databinding.ActivityHomeBinding
import ru.nsu.merkuriev.waterbalance.domain.model.NotificationData
import ru.nsu.merkuriev.waterbalance.domain.model.NotificationType
import ru.nsu.merkuriev.waterbalance.presentation.common.view.BaseToolbarActivity
import ru.nsu.merkuriev.waterbalance.presentation.home.viewmodel.HomeViewModel
import ru.nsu.merkuriev.waterbalance.presentation.receiver.ResetWaterBalanceBroadcastReceiver
import ru.nsu.merkuriev.waterbalance.presentation.receiver.WaterBalanceNotificationBroadcastReceiver
import ru.nsu.merkuriev.waterbalance.utils.general.AlarmManagerUtils
import ru.nsu.merkuriev.waterbalance.utils.general.MetricsUtils
import ru.nsu.merkuriev.waterbalance.utils.general.TimeUtils
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.pure.AppNavigator

class HomeActivity : BaseToolbarActivity<ActivityHomeBinding>() {

    override val navigator: Navigator = AppNavigator(this, 0)

    override fun getLayoutId(): Int = R.layout.activity_home

    override val viewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun initToolbar() {
        super.initToolbar()
        setToolbarTitle(R.string.home_screen_title)

        addItem(R.drawable.ic_settings, action = { viewModel.openSettingScreen() })
        addItem(R.drawable.ic_update, action = { showResetWaterBalanceDialog() })
    }

    override fun initUI() {
        super.initUI()

        setResetWaterBalanceAlarm()

        binding.morningCheckbox.isChecked =
            viewModel.isNotificationTypeEnabled(NotificationType.MORNING)

        binding.dayCheckbox.isChecked =
            viewModel.isNotificationTypeEnabled(NotificationType.DAY)

        binding.eveningCheckbox.isChecked =
            viewModel.isNotificationTypeEnabled(NotificationType.EVENING)

        binding.fab.setOnClickListener {
            showAddWaterDialog()
        }

        binding.morningTime.setOnClickListener {
            showTimePickerDialog(viewModel.getNotificationData(NotificationType.MORNING))
        }

        binding.dayTime.setOnClickListener {
            showTimePickerDialog(viewModel.getNotificationData(NotificationType.DAY))
        }

        binding.eveningTime.setOnClickListener {
            showTimePickerDialog(viewModel.getNotificationData(NotificationType.EVENING))
        }

        binding.morningCheckbox.setOnCheckedChangeListener { _, isChecked ->
            handleCheckBoxChange(isChecked, NotificationType.MORNING)
        }

        binding.dayCheckbox.setOnCheckedChangeListener { _, isChecked ->
            handleCheckBoxChange(isChecked, NotificationType.DAY)
        }

        binding.eveningCheckbox.setOnCheckedChangeListener { _, isChecked ->
            handleCheckBoxChange(isChecked, NotificationType.EVENING)
        }

        viewModel.getDrinkWaterProportion().observe(this) {
            binding.bottle.setLevel(it)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        viewModel.loadUser()
    }

    private fun handleCheckBoxChange(isChecked: Boolean, notificationType: NotificationType) {
        if (isChecked) {
            setAlarm(viewModel.getNotificationData(notificationType))
        } else {
            cancelAlarm(notificationType)
        }
        viewModel.setNotificationTypeEnabled(isChecked, notificationType)
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
            addAll(HomeViewModel.waterValuesToAdd.map {
                MetricsUtils.convertMilliliterToOunceIfNecessary(it).toString()
            })
        }

        AlertDialog.Builder(this)
            .apply {
                setTitle(getString(R.string.home_screen_dialog_add_water))
                setItems(localizedWaterValues.toTypedArray()) { _, which ->
                    viewModel.addDrinkWater(HomeViewModel.waterValuesToAdd[which])
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
        val pendingIntent = createPendingIntent(data.notificationType)
        AlarmManagerUtils.setNotificationAlarm(this, pendingIntent, data.hour, data.minute)

        Toast.makeText(
            this,
            getString(
                R.string.dialog_alarm_set,
                TimeUtils.formatTimeIntToString24(data.hour),
                TimeUtils.formatTimeIntToString24(data.minute)
            ),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun cancelAlarm(notificationType: NotificationType) {
        AlarmManagerUtils.cancelAlarm(this, createPendingIntent(notificationType))
    }

    private fun createPendingIntent(notificationType: NotificationType): PendingIntent {
        val intent = WaterBalanceNotificationBroadcastReceiver.getIntent(this)
        return PendingIntent.getBroadcast(this, notificationType.ordinal, intent, 0)
    }

    private fun setResetWaterBalanceAlarm() {
        val intent = ResetWaterBalanceBroadcastReceiver.getIntent(this)
        val resetIntent = PendingIntent.getBroadcast(this, 666, intent, 0)

        AlarmManagerUtils.setResetWaterBalanceAlarm(this, resetIntent)
    }
}
