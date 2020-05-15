package ru.nsu.merkuriev.waterbalance.presentation.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nsu.merkuriev.waterbalance.data.repository.UserRepository
import ru.nsu.merkuriev.waterbalance.domain.model.ActiveType
import ru.nsu.merkuriev.waterbalance.domain.model.User
import ru.nsu.merkuriev.waterbalance.presentation.common.viewmodel.BaseViewModel
import ru.nsu.merkuriev.waterbalance.utils.rx.RxSchedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    router: Router,
    schedulers: RxSchedulers,
    private val repository: UserRepository
) : BaseViewModel(router, schedulers) {

    private val selectedActiveType = MutableLiveData(ActiveType.NO)
    val name = MutableLiveData("")
    val weight = MutableLiveData("")

    fun getSelectedActiveType(): LiveData<ActiveType> = selectedActiveType

    override fun initialize() {
        super.initialize()

        loadUser()
    }

    fun save() {
        disposable.add(
            repository.updateUser(
                User(
                    name.value.orEmpty(),
                    weight.value.orEmpty().toFloatOrNull() ?: 0f,
                    selectedActiveType.value ?: ActiveType.NO
                )
            ).compose(schedulers.fromIOToMainTransformerCompletable())
                .subscribe({ router.exit() }, { })
        )
    }

    fun setSelectedActiveType(activeType: ActiveType) {
        selectedActiveType.value = activeType
    }

    private fun loadUser() {
        disposable.add(
            repository.getUser()
                .compose(schedulers.fromIOToMainTransformerSingle())
                .subscribe(
                    {
                        name.value = it.name
                        weight.value = it.weight.toString()
                        selectedActiveType.value = it.activeType
                    },
                    {

                    }
                )
        )
    }
}