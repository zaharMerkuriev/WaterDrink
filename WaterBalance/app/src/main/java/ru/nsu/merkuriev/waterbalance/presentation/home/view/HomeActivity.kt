package ru.nsu.merkuriev.waterbalance.presentation.home.view

import androidx.activity.viewModels
import ru.nsu.merkuriev.waterbalance.R
import ru.nsu.merkuriev.waterbalance.databinding.ActivityHomeBinding
import ru.nsu.merkuriev.waterbalance.presentation.common.view.BaseToolbarActivity
import ru.nsu.merkuriev.waterbalance.presentation.home.viewmodel.HomeViewModel
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.pure.AppNavigator

class HomeActivity : BaseToolbarActivity<ActivityHomeBinding>() {

    override val navigator: Navigator = AppNavigator(this, 0)

    override fun getLayoutId(): Int = R.layout.activity_home

    override val viewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun initToolbar() {
        super.initToolbar()
        setToolbarTitle(R.string.home_screen_title)
    }

    override fun initUI() {
        super.initUI()

        binding.fab.setOnClickListener {
            binding.bottle.setLevel(binding.bottle.getLevel() + 5f)
        }

    }
}
