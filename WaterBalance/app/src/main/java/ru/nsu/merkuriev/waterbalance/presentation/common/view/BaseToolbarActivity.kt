package ru.nsu.merkuriev.waterbalance.presentation.common.view

import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import ru.nsu.merkuriev.waterbalance.R

abstract class BaseToolbarActivity<Binding : ViewDataBinding> : BaseActivity<Binding>() {

    override fun initUI() {
        super.initUI()
        initToolbar()
    }

    @CallSuper
    protected open fun initToolbar() {
        setSupportActionBar(binding.root.findViewById(R.id.toolbar))
    }

    protected fun setToolbarTitle(@StringRes title: Int) {
        supportActionBar?.title = getString(title)
    }
}