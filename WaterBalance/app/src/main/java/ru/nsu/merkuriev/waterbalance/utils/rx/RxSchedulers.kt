package ru.nsu.merkuriev.waterbalance.utils.rx

import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RxSchedulers {

    fun getIOScheduler(): Scheduler = Schedulers.io()

    fun getMainSchedulers(): Scheduler = AndroidSchedulers.mainThread()

    fun <T> fromIOToMainTransformerSingle(): ObservableTransformer<T, T> =
        ObservableTransformer { upstream ->
            upstream.subscribeOn(getIOScheduler()).observeOn(getMainSchedulers())
        }
}