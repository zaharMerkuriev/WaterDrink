package ru.nsu.merkuriev.waterbalance.presentation.create_user.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import ru.nsu.merkuriev.waterbalance.domain.model.ActiveType
import ru.nsu.merkuriev.waterbalance.presentation.HomeScreen
import ru.nsu.merkuriev.waterbalance.presentation.common.viewmodel.BaseViewModel
import ru.nsu.merkuriev.waterbalance.utils.extensions.combine
import ru.nsu.merkuriev.waterbalance.utils.rx.RxSchedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class CreateUserViewModel @Inject constructor(
    router: Router,
    rxSchedulers: RxSchedulers,
    private val prefs: SharedPreferences
) : BaseViewModel(router, rxSchedulers) {

    private val selectedActiveType = MutableLiveData(ActiveType.NO)
    val name = MutableLiveData("")
    val weight = MutableLiveData("")

    private val isDataValid = MediatorLiveData<Boolean>().apply {
        combine(name, weight, selectedActiveType) { name, weight, activeType ->
            name.isNullOrBlank().not()
                    && weight.isNullOrBlank().not()
                    && activeType != ActiveType.NO
        }
    }

    fun getIsDataValid(): LiveData<Boolean> = isDataValid

    override fun initialize() {
        super.initialize()
        checkIsFirstOpen()
    }

    fun onNextClick() {
        prefs.edit().putBoolean(KEY_FIRST_OPEN, false).apply()
        router.newRootScreen(HomeScreen())
    }

    fun setSelectedActiveType(activeType: ActiveType) {
        selectedActiveType.value = activeType
    }

    private fun checkIsFirstOpen() {
        if (prefs.getBoolean(KEY_FIRST_OPEN, true).not()) {
            router.newRootScreen(HomeScreen())
        }
    }

    companion object {
        private const val KEY_FIRST_OPEN = "KEY_FIRST_OPEN"
    }
}