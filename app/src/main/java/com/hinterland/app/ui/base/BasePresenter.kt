package com.hinterland.app.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BasePresenter<V : BaseView> {
    protected var view: V? = null

    // Coroutine scope and job for managing coroutines
    private val presenterJob = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Main + presenterJob)

    open fun attachView(view: V) {
        this.view = view
    }

    open fun detachView() {
        this.view = null
    }
}