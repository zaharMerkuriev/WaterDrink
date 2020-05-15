package ru.nsu.merkuriev.waterbalance.presentation.common.view

import android.view.Gravity
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import ru.nsu.merkuriev.waterbalance.R
import ru.nsu.merkuriev.waterbalance.databinding.ItemToolbarBinding


abstract class BaseToolbarActivity<Binding : ViewDataBinding> : BaseActivity<Binding>() {

    private var isNavigationBackEnabled = false

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

    protected fun setNavigationBack(show: Boolean) {
        isNavigationBackEnabled = show
        supportActionBar?.setDisplayHomeAsUpEnabled(isNavigationBackEnabled)
    }

    protected fun addItem(
        @DrawableRes iconId: Int,
        @ColorRes iconColor: Int = R.color.colorWhite,
        action: (() -> Unit)? = null
    ) {
        (binding.root.findViewById(R.id.toolbar) as? Toolbar)?.apply {
            val binding = ItemToolbarBinding.inflate(layoutInflater)
            binding.icon = getDrawable(iconId)
            binding.iconColor = iconColor

            binding.root.setOnClickListener { action?.invoke() }

            val params = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            )
            params.marginEnd = resources.getDimensionPixelSize(R.dimen.margin_medium)
            params.gravity = Gravity.END
            binding.root.layoutParams = params

            addView(binding.root)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (isNavigationBackEnabled) {
            onBackPressed()
        }
        return super.onSupportNavigateUp()
    }
}