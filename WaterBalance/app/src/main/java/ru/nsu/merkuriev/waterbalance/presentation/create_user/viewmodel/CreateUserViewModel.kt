package ru.nsu.merkuriev.waterbalance.presentation.create_user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import ru.nsu.merkuriev.waterbalance.data.repository.SharedPreferencesRepository
import ru.nsu.merkuriev.waterbalance.data.repository.UserRepository
import ru.nsu.merkuriev.waterbalance.domain.model.ActiveType
import ru.nsu.merkuriev.waterbalance.domain.model.User
import ru.nsu.merkuriev.waterbalance.presentation.HomeScreen
import ru.nsu.merkuriev.waterbalance.presentation.common.viewmodel.BaseViewModel
import ru.nsu.merkuriev.waterbalance.utils.extensions.combine
import ru.nsu.merkuriev.waterbalance.utils.rx.RxSchedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class CreateUserViewModel @Inject constructor(
    router: Router,
    rxSchedulers: RxSchedulers,
    private val prefsRepository: SharedPreferencesRepository,
    private val repository: UserRepository
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
    fun getSelectedActiveType(): LiveData<ActiveType> = selectedActiveType

    override fun initialize() {
        super.initialize()
        checkIsFirstOpen()
    }

    fun onNextClick() {
        disposable.add(
            repository.createUser(
                User(
                    name.value.orEmpty(),
                    weight.value.orEmpty().toFloatOrNull() ?: 0f,
                    selectedActiveType.value ?: ActiveType.NO
                )
            ).compose(schedulers.fromIOToMainTransformerCompletable())
                .subscribe(
                    {
                        handleSuccessCreation()
                    },
                    {

                    }
                )
        )
    }

    fun setSelectedActiveType(activeType: ActiveType) {
        selectedActiveType.value = activeType
    }

    private fun handleSuccessCreation() {
        prefsRepository.setIsAppFirstOpen(false)
        router.newRootScreen(HomeScreen())
    }

    private fun checkIsFirstOpen() {
        if (prefsRepository.isAppFirstOpen().not()) {
            router.newRootScreen(HomeScreen())
        }
    }
}