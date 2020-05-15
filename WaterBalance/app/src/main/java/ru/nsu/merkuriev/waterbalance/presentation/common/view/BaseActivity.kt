package ru.nsu.merkuriev.waterbalance.presentation.common.view

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.android.AndroidInjection
import ru.nsu.merkuriev.bodysign.di.ViewModelFactory
import ru.nsu.merkuriev.waterbalance.BR
import ru.nsu.merkuriev.waterbalance.R
import ru.nsu.merkuriev.waterbalance.presentation.common.viewmodel.IBaseViewModel
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity() {

    @Inject
    lateinit var navigationHolder: NavigatorHolder

    protected abstract val navigator: Navigator

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected abstract val viewModel: ViewModel

    protected lateinit var binding: Binding

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        if (savedInstanceState == null) {
            initViewModel()
        }

        initUI()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }

    private fun initViewModel() {
        (viewModel as IBaseViewModel).onFirstAttach()
    }

    @CallSuper
    protected open fun initUI() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
        binding.setVariable(BR.model, viewModel)
    }

    fun showActionDialog(
        title: String?,
        message: String,
        @StringRes positiveButtonRes: Int = R.string.dialog_button_ok,
        @StringRes negativeButtonRes: Int = R.string.dialog_button_cancel,
        positiveAction: (() -> Unit)? = null,
        negativeAction: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(this)
            .apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton(positiveButtonRes) { _, _ -> positiveAction?.invoke() }
                setNegativeButton(negativeButtonRes) { _, _ -> negativeAction?.invoke() }
                setCancelable(false)
            }.show()
    }
}