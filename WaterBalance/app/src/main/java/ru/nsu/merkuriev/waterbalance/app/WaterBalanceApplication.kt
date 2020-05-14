package ru.nsu.merkuriev.waterbalance.app

import dagger.android.DaggerApplication
import ru.nsu.merkuriev.waterbalance.di.DaggerAppComponent

class WaterBalanceApplication : DaggerApplication() {
    override fun applicationInjector() = DaggerAppComponent.factory().create(this)
}