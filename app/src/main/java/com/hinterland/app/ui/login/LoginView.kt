package com.hinterland.app.ui.login

import com.hinterland.app.ui.base.BaseView

interface LoginView : BaseView {
    fun getString(resId: Int): String
    fun landingScreen()
}




