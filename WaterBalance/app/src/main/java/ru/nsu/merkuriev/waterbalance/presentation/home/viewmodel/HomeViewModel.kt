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

    private val dayHour = MutableLiveData<String>()
    private val dayMinute = MutableLiveData<String>()
    private var isDayEnabled = false

    private val eveningHour = MutableLiveData<String>()
    private val eveningMinute = MutableLiveData<String>()
    private var isEveningEnabled = false

    fun getRemainWater(): LiveData<Float> = remainWater
    fun getDrinkWater(): LiveData<Float> = drinkWater
    fun getDrinkWaterProportion(): LiveData<Float> = drinkWaterProportion

    fun getMorningHour(): LiveData<String> = morningHour
    fun getMorningMinute(): LiveData<String> = morningMinute

    fun getDayHour(): LiveData<String> = dayHour
    fun getDayMinute(): LiveData<String> = dayMinute

    fun getEveningHour(): LiveData<String> = eveningHour
    fun getEveningMinute(): LiveData<String> = eveningMinute

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
            NotificationType.DAY -> NotificationData(
                notificationType,
                getDayHour().value.toIntOrZero(),
                getDayMinute().value.toIntOrZero()
            )
            NotificationType.EVENING -> NotificationData(
                notificationType,
                getEveningHour().value.toIntOrZero(),
                getEveningMinute().value.toIntOrZero()
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
            NotificationType.DAY -> {
                setHourAndMinute(dayHour, dayMinute, hour, minute)
                prefsRepository.setDayHour(hour)
                prefsRepository.setDayMinute(minute)
            }
            NotificationType.EVENING -> {
                setHourAndMinute(eveningHour, eveningMinute, hour, minute)
                prefsRepository.setEveningHour(hour)
                prefsRepository.setEveningMinute(minute)
            }
        }
    }

    fun setNotificationTypeEnabled(value: Boolean, notificationType: NotificationType) {
        when (notificationType) {
            NotificationType.MORNING -> {
                isMorningEnabled = value
                prefsRepository.setMorningEnabled(value)
            }
            NotificationType.DAY -> {
                isDayEnabled = value
                prefsRepository.setDayEnabled(value)
            }
            NotificationType.EVENING -> {
                isEveningEnabled = value
                prefsRepository.setEveningEnabled(value)
            }
        }
    }

    fun isNotificationTypeEnabled(notificationType: NotificationType) =
        when (notificationType) {
            NotificationType.MORNING -> isMorningEnabled
            NotificationType.DAY -> isDayEnabled
            NotificationType.EVENING -> isEveningEnabled
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

        setTime(
            prefsRepository.getDayHour(),
            prefsRepository.getDayMinute(),
            NotificationType.DAY
        )
        isDayEnabled = prefsRepository.isDayAlarmEnabled()

        setTime(
            prefsRepository.getEveningHour(),
            prefsRepository.getEveningMinute(),
            NotificationType.EVENING
        )
        isEveningEnabled = prefsRepository.isEveningAlarmEnabled()
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