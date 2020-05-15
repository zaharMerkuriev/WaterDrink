package ru.nsu.merkuriev.waterbalance.presentation.settings.view

import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.observe
import ru.nsu.merkuriev.waterbalance.R
import ru.nsu.merkuriev.waterbalance.databinding.ActivitySettingsBinding
import ru.nsu.merkuriev.waterbalance.domain.model.ActiveType
import ru.nsu.merkuriev.waterbalance.presentation.common.view.BaseToolbarActivity
import ru.nsu.merkuriev.waterbalance.presentation.common.view.adapter.ActiveTypeAdapter
import ru.nsu.merkuriev.waterbalance.presentation.settings.viewmodel.SettingsViewModel
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.pure.AppNavigator

class SettingsActivity : BaseToolbarActivity<ActivitySettingsBinding>() {

    override val navigator: Navigator = AppNavigator(this, 0)

    override val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun getLayoutId(): Int = R.layout.activity_settings

    private val activeTypes =
        arrayListOf(ActiveType.LAZY, ActiveType.MIDDLE, ActiveType.ACTIVE)

    override fun initToolbar() {
        super.initToolbar()
        setToolbarTitle(R.string.settings_screen_title)
        setNavigationBack(true)
    }

    override fun initUI() {
        super.initUI()

        initSpinner()

        binding.save.setOnClickListener { viewModel.save() }

        viewModel.getSelectedActiveType().observe(this) {
            binding.activeType.setText(getString(it.title), false)
        }
    }

    private fun initSpinner() {
        binding.activeType.setAdapter(
            ActiveTypeAdapter(
                this,
                R.layout.item_active_type,
                activeTypes
            )
        )

        binding.activeType.setOnItemClickListener { parent, view, position, id ->
            (parent.adapter as? ArrayAdapter<ActiveType>)?.getItem(position)?.let {
                viewModel.setSelectedActiveType(it)
            }
            binding.activeType.clearFocus()
        }
    }
}