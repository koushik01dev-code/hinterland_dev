package com.hinterland.app

import android.app.Application
import com.hinterland.app.components.network.RetrofitWrapper

class HinterLand : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitWrapper.instance.setBaseUrl("http://10.0.2.2:8080/")
    }
}
