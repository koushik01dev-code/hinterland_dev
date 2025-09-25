package com.hinterland.app.ui.base

import com.hinterland.app.components.dialog.AlertType

interface BaseView {
    fun showMessage(message: String)
    fun showCustomAlert(
        message: String,
        title: String? = null,
        alertType: AlertType,
        callback: (() -> Unit)? = null
    )
    fun showLoader()
    fun hideLoader()
}







