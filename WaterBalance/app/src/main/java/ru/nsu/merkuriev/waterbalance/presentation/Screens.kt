package ru.nsu.merkuriev.waterbalance.presentation

import android.content.Context
import android.content.Intent
import ru.nsu.merkuriev.waterbalance.presentation.home.view.HomeActivity
import ru.terrakok.cicerone.android.pure.AppScreen

class HomeScreen : AppScreen() {

    override fun getActivityIntent(context: Context?): Intent {
        return Intent(context, HomeActivity::class.java)
    }
}