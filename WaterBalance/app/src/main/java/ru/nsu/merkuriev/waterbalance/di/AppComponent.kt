package ru.nsu.merkuriev.waterbalance.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.nsu.merkuriev.waterbalance.app.WaterBalanceApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        CiceroneModule::class,
        CommonModule::class,
        UIModule::class
    ]
)
interface AppComponent : AndroidInjector<WaterBalanceApplication> {

    @Component.Factory
    interface Builder {
        fun create(@BindsInstance context: Context): AppComponent
    }
}