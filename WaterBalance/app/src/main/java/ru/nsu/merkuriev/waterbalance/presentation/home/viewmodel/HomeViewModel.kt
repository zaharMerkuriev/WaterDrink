package ru.nsu.merkuriev.waterbalance.presentation.home.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nsu.merkuriev.waterbalance.data.repository.UserRepository
import ru.nsu.merkuriev.waterbalance.domain.model.User
import ru.nsu.merkuriev.waterbalance.presentation.SettingsScreen
import ru.nsu.merkuriev.waterbalance.presentation.common.viewmodel.BaseViewModel
import ru.nsu.merkuriev.waterbalance.utils.rx.RxSchedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class HomeViewModel @Inject constructor(
    router: Router,
    schedulers: RxSchedulers,
    private val prefs: SharedPreferences,
    private val repository: UserRepository
) : BaseViewModel(router, schedulers) {

    private lateinit var user: User

    private val remainWater = MutableLiveData<Float>()
    private val drinkWater = MutableLiveData<Float>()

    private val drinkWaterProportion = MutableLiveData<Float>()

    fun getRemainWater(): LiveData<Float> = remainWater
    fun getDrinkWater(): LiveData<Float> = drinkWater
    fun getDrinkWaterProportion(): LiveData<Float> = drinkWaterProportion


    override fun initialize() {
        super.initialize()

        loadUser()
    }

    fun resetWaterBalance() {
        updateDrinkWaterValue(0f)
        calculateWaterBalance()
    }

    fun addDrinkWater(value: Float) {
        updateDrinkWaterValue(getDrinkWaterValue() + value)
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

    private fun calculateWaterBalance() {
        val allWater = user.weight * WEIGHT_COEFFICIENT + user.activeType.waterSum
        val currentDrinkWater = getDrinkWaterValue()

        remainWater.value = max(allWater - currentDrinkWater, 0f)
        drinkWater.value = getDrinkWaterValue()

        drinkWaterProportion.value = min(currentDrinkWater / allWater * 100, 100f)
    }

    private fun getDrinkWaterValue() = prefs.getFloat(KEY_DRINK_WATER_VALUE, 0f)

    private fun updateDrinkWaterValue(value: Float) =
        prefs.edit().putFloat(KEY_DRINK_WATER_VALUE, value).apply()

    companion object {
        private const val KEY_DRINK_WATER_VALUE = "KEY_DRINK_WATER_VALUE"
        private const val WEIGHT_COEFFICIENT = 30f
    }
}