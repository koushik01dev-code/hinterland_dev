package com.hinterland.app.ui.register

import com.hinterland.app.ui.base.BaseView

interface RegisterView : BaseView {
    fun onRegisterSuccess()
    fun onRegisterError(message: String)
}
