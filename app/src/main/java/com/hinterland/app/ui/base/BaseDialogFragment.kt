package com.hinterland.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseDialogFragment<P : Any, V, VB : ViewBinding> : DialogFragment() {
    protected lateinit var binding: VB
    protected lateinit var presenter: P

    abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun providePresenter(): P
    abstract fun provideView(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = providePresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = provideViewBinding(inflater, container)
        return binding.root
    }
}

