package ru.nsu.merkuriev.waterbalance.presentation.home.view

import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.observe
import ru.nsu.merkuriev.waterbalance.R
import ru.nsu.merkuriev.waterbalance.databinding.ActivityHomeBinding
import ru.nsu.merkuriev.waterbalance.presentation.common.view.BaseToolbarActivity
import ru.nsu.merkuriev.waterbalance.presentation.home.viewmodel.HomeViewModel
import ru.nsu.merkuriev.waterbalance.utils.ui.MetricsUtils
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.pure.AppNavigator

class HomeActivity : BaseToolbarActivity<ActivityHomeBinding>() {

    override val navigator: Navigator = AppNavigator(this, 0)

    override fun getLayoutId(): Int = R.layout.activity_home

    override val viewModel: HomeViewModel by viewModels { viewModelFactory }

    private val waterValues = arrayOf(100f, 200f, 400f)

    override fun initToolbar() {
        super.initToolbar()
        setToolbarTitle(R.string.home_screen_title)

        addItem(R.drawable.ic_settings, action = { viewModel.openSettingScreen() })
        addItem(R.drawable.ic_update, action = { showResetWaterBalanceDialog() })
    }

    override fun initUI() {
        super.initUI()

        binding.fab.setOnClickListener {
            showAddWaterDialog()
        }

        viewModel.getDrinkWaterProportion().observe(this) {
            binding.bottle.setLevel(it)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        viewModel.loadUser()
    }

    private fun showResetWaterBalanceDialog() {
        showActionDialog(
            getString(R.string.home_screen_dialog_reset_title),
            getString(R.string.home_screen_dialog_reset_message),
            positiveAction = { viewModel.resetWaterBalance() }
        )
    }

    private fun showAddWaterDialog() {
        val localizedWaterValues = arrayListOf<String>().apply {
            addAll(waterValues.map {
                MetricsUtils.convertMilliliterToOunceIfNecessary(it).toString()
            })
        }

        AlertDialog.Builder(this)
            .apply {
                setTitle(getString(R.string.home_screen_dialog_add_water))
                setItems(localizedWaterValues.toTypedArray()) { _, which ->
                    viewModel.addDrinkWater(waterValues[which])
                }
            }.show()
    }
}
