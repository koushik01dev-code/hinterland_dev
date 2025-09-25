package com.hinterland.app.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hinterland.app.components.dialog.AlertDialog
import com.hinterland.app.components.dialog.AlertType
import com.hinterland.app.components.views.LoaderView

abstract class BaseFragment<P : BasePresenter<V>, V : BaseView, VB : ViewBinding> : Fragment(), BaseView {

    protected var mContext: Context? = null
    protected lateinit var mPresenter: P
    protected lateinit var binding: VB
    private lateinit var loaderView: LoaderView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = provideViewBinding(inflater, container) // Initialize view binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    // Method to show the custom loader
    override fun showLoader() {
        loaderView.show()
    }

    // Method to hide the custom loader
    override fun hideLoader() {
        loaderView.hide()
    }

    private fun init() {
        mPresenter = providePresenter()
        mPresenter.attachView(provideView())
    }

    override fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }

    fun showMessage(message: String?, duration: Int) {
        Toast.makeText(mContext, message, duration).show()
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
        alertDialog.show(childFragmentManager, "AlertPopup")
    }
    protected fun replaceCurrentFragment(
        @IdRes container: Int,
        fragment: BaseFragment<*, *, *>,
        addToBackStack: Boolean
    ) {
        (activity as? BaseActivity<*, *, *>)?.showFragment(container, fragment, addToBackStack)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.detachView()
    }

    protected fun finish() {
        requireActivity().supportFragmentManager.popBackStack()
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        requireActivity().currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun provideTag(): String = javaClass.name

    protected abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    protected abstract fun providePresenter(): P
    protected abstract fun provideView(): V

    protected fun setOnClickListeners(listener: View.OnClickListener, vararg views: View) {
        views.forEach { it.setOnClickListener(listener) }
    }
}