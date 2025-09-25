package com.hinterland.app.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import android.content.Context
import androidx.annotation.IdRes
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentManager
import com.hinterland.app.components.dialog.AlertDialog
import com.hinterland.app.components.dialog.AlertType
import com.hinterland.app.components.lang.LocaleManager
import com.hinterland.app.components.views.LoaderView

abstract class BaseActivity<P : BasePresenter<V>, V : BaseView, VB : ViewBinding> :
    AppCompatActivity(), BaseView {

    protected open lateinit var binding: VB
    protected open lateinit var presenter: P
    private lateinit var loaderView: LoaderView

    protected abstract fun provideViewBinding(): VB
    protected abstract fun providePresenter(): P
    protected abstract fun provideView(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = provideViewBinding()
        setContentView(binding.root)
        loaderView = LoaderView(this)
        presenter = providePresenter()
        presenter.attachView(provideView())
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(newBase)
            return
        }
        val wrapped = LocaleManager.wrap(newBase)
        super.attachBaseContext(wrapped)
    }

    fun showFragment(@IdRes container: Int, fragment: BaseFragment<*, *, *>, addToBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        if (addToBackStack) {
            transaction.add(container, fragment, fragment.provideTag())
                .addToBackStack(fragment.provideTag())
        } else {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            transaction.replace(container, fragment, fragment.provideTag())
        }

        transaction.commit()
    }

    //show custom alert popup
    override fun showCustomAlert(
        message: String,
        title: String?,
        alertType: AlertType,
        callback: (() -> Unit)?
    ) {
        val alertDialog = AlertDialog().apply {
            setTitle(title)
            setMessage(message)
            setAlertType(alertType)
            setOkButtonCallback {
                callback?.invoke()
            }
        }
        alertDialog.show(supportFragmentManager, "AlertPopup")
    }

    // Method to show the custom loader
    override fun showLoader() {
        loaderView.show()
    }

    // Method to hide the custom loader
    override fun hideLoader() {
        loaderView.hide()
    }
}



