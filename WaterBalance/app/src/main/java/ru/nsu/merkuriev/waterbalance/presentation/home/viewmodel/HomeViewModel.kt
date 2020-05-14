package ru.nsu.merkuriev.waterbalance.presentation.home.viewmodel

import ru.nsu.merkuriev.waterbalance.presentation.common.viewmodel.BaseViewModel
import ru.nsu.merkuriev.waterbalance.utils.rx.RxSchedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    router: Router,
    schedulers: RxSchedulers
) : BaseViewModel(router, schedulers) {

    override fun initialize() {
        super.initialize()


    }
}