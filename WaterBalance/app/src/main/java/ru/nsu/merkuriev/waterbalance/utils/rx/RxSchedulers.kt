package ru.nsu.merkuriev.waterbalance.utils.rx

import io.reactivex.CompletableTransformer
import io.reactivex.Scheduler
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RxSchedulers {

    fun getIOScheduler(): Scheduler = Schedulers.io()

    fun getMainSchedulers(): Scheduler = AndroidSchedulers.mainThread()

    fun <T> fromIOToMainTransformerSingle(): SingleTransformer<T, T> =
        SingleTransformer { upstream ->
            upstream.subscribeOn(getIOScheduler()).observeOn(getMainSchedulers())
        }

    fun fromIOToMainTransformerCompletable(): CompletableTransformer =
        CompletableTransformer { upstream ->
            upstream.subscribeOn(getIOScheduler()).observeOn(getMainSchedulers())
        }
}