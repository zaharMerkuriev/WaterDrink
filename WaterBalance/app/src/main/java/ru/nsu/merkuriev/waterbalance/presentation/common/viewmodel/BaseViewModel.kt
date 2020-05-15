package ru.nsu.merkuriev.waterbalance.presentation.common.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.nsu.merkuriev.waterbalance.utils.rx.RxSchedulers
import ru.terrakok.cicerone.Router

abstract class BaseViewModel(
    protected val router: Router,
    protected val schedulers: RxSchedulers
) : ViewModel(), IBaseViewModel {

    protected val disposable = CompositeDisposable()

    private var isInit = false

    override fun onFirstAttach() {
        if (isInit) {
            return
        }

        initialize()

        isInit = true
    }

    protected open fun initialize() {

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}