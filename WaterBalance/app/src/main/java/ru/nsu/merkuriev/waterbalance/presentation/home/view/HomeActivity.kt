package ru.nsu.merkuriev.waterbalance.presentation.home.view

import androidx.activity.viewModels
import androidx.lifecycle.observe
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

        addItem(R.drawable.ic_settings)
        addItem(R.drawable.ic_update, action = { showResetWaterBalanceDialog() })
    }

    override fun initUI() {
        super.initUI()

        binding.fab.setOnClickListener {
            // TODO : choose value
            viewModel.addDrinkWater(100f)
        }

        viewModel.getDrinkWaterProportion().observe(this) {
            binding.bottle.setLevel(it)
        }
    }

    private fun showResetWaterBalanceDialog() {
        showActionDialog(
            getString(R.string.home_screen_dialog_reset_title),
            getString(R.string.home_screen_dialog_reset_message),
            positiveAction = { viewModel.resetWaterBalance() }
        )
    }
}
