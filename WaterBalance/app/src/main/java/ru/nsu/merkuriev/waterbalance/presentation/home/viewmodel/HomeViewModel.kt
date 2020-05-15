package ru.nsu.merkuriev.waterbalance.presentation.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nsu.merkuriev.waterbalance.data.repository.SharedPreferencesRepository
import ru.nsu.merkuriev.waterbalance.data.repository.UserRepository
import ru.nsu.merkuriev.waterbalance.domain.model.NotificationData
import ru.nsu.merkuriev.waterbalance.domain.model.NotificationType
import ru.nsu.merkuriev.waterbalance.domain.model.User
import ru.nsu.merkuriev.waterbalance.presentation.SettingsScreen
import ru.nsu.merkuriev.waterbalance.presentation.common.viewmodel.BaseViewModel
import ru.nsu.merkuriev.waterbalance.utils.extensions.toIntOrZero
import ru.nsu.merkuriev.waterbalance.utils.general.MetricsUtils
import ru.nsu.merkuriev.waterbalance.utils.general.TimeUtils
import ru.nsu.merkuriev.waterbalance.utils.rx.RxSchedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class HomeViewModel @Inject constructor(
    router: Router,
    schedulers: RxSchedulers,
    private val prefsRepository: SharedPreferencesRepository,
    private val repository: UserRepository
) : BaseViewModel(router, schedulers) {

    private lateinit var user: User

    private val remainWater = MutableLiveData<Float>()
    private val drinkWater = MutableLiveData<Float>()

    private val drinkWaterProportion = MutableLiveData<Float>()

    private val morningHour = MutableLiveData<String>()
    private val morningMinute = MutableLiveData<String>()
    private var isMorningEnabled = false

    fun getRemainWater(): LiveData<Float> = remainWater
    fun getDrinkWater(): LiveData<Float> = drinkWater
    fun getDrinkWaterProportion(): LiveData<Float> = drinkWaterProportion

    fun getMorningHour(): LiveData<String> = morningHour
    fun getMorningMinute(): LiveData<String> = morningMinute

    override fun initialize() {
        super.initialize()

        initNotificationsTimes()
        loadUser()
    }

    fun getNotificationData(notificationType: NotificationType) =
        when (notificationType) {
            NotificationType.MORNING -> NotificationData(
                notificationType,
                getMorningHour().value.toIntOrZero(),
                getMorningMinute().value.toIntOrZero()
            )
            else -> NotificationData(
                notificationType,
                getMorningHour().value.toIntOrZero(),
                getMorningMinute().value.toIntOrZero()
            )
        }


    fun resetWaterBalance() {
        prefsRepository.setDrinkWaterValue(0f)
        calculateWaterBalance()
    }

    /**
     * Add water in milliliters
     */
    fun addDrinkWater(value: Float) {
        prefsRepository.setDrinkWaterValue(prefsRepository.getDrinkWaterValue() + value)
        calculateWaterBalance()
    }

    fun openSettingScreen() {
        router.navigateTo(SettingsScreen())
    }

    fun loadUser() {
        disposable.add(
            repository.getUser()
                .compose(schedulers.fromIOToMainTransformerSingle<User>())
                .subscribe(
                    {
                        user = it
                        calculateWaterBalance()
                    },
                    {

                    }
                )
        )
    }

    fun setTime(hour: Int, minute: Int, notificationType: NotificationType) {
        when (notificationType) {
            NotificationType.MORNING -> {
                setHourAndMinute(morningHour, morningMinute, hour, minute)
                prefsRepository.setMorningHour(hour)
                prefsRepository.setMorningMinute(minute)
            }
        }
    }

    fun setNotificationTypeEnabled(value: Boolean, notificationType: NotificationType) {
        when (notificationType) {
            NotificationType.MORNING -> {
                isMorningEnabled = value
                prefsRepository.setMorningEnabled(value)
            }
        }
    }

    fun isNotificationTypeEnabled(notificationType: NotificationType) =
        when (notificationType) {
            NotificationType.MORNING -> isMorningEnabled
            else -> isMorningEnabled
        }


    private fun setHourAndMinute(
        hourSource: MutableLiveData<String>,
        minuteSource: MutableLiveData<String>,
        hour: Int,
        minute: Int
    ) {
        hourSource.value = TimeUtils.formatTimeIntToString24(hour)
        minuteSource.value = TimeUtils.formatTimeIntToString24(minute)
    }

    private fun initNotificationsTimes() {
        setTime(
            prefsRepository.getMorningHour(),
            prefsRepository.getMorningMinute(),
            NotificationType.MORNING
        )
        isMorningEnabled = prefsRepository.isMorningAlarmEnabled()
    }

    private fun calculateWaterBalance() {
        val allWater = user.weight * WEIGHT_COEFFICIENT + user.activeType.waterSum
        val currentDrinkWater = prefsRepository.getDrinkWaterValue()

        remainWater.value =
            MetricsUtils.convertMilliliterToOunceIfNecessary(max(allWater - currentDrinkWater, 0f))
        drinkWater.value =
            MetricsUtils.convertMilliliterToOunceIfNecessary(prefsRepository.getDrinkWaterValue())

        drinkWaterProportion.value = min(currentDrinkWater / allWater * 100, 100f)
    }

    companion object {
        private const val WEIGHT_COEFFICIENT = 30f
    }
}