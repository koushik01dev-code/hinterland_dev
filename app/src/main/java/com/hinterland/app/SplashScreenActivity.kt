package com.hinterland.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.hinterland.app.ui.base.BaseActivity
import com.hinterland.app.ui.base.BaseInteractor
import com.hinterland.app.ui.base.BasePresenter
import com.hinterland.app.ui.base.BaseView
import com.hinterland.app.databinding.ActivitySplashBinding
import com.hinterland.app.ui.login.LoginActivity

interface SplashScreenView : BaseView

class SplashScreenInteractor : BaseInteractor

class SplashScreenPresenter : BasePresenter<SplashScreenView>()

class SplashScreenActivity : BaseActivity<SplashScreenPresenter, SplashScreenView, ActivitySplashBinding>(),
    SplashScreenView {

    override fun provideViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun providePresenter(): SplashScreenPresenter {
        return SplashScreenPresenter()
    }

    override fun provideView(): SplashScreenView {
        return this
    }

    override fun showMessage(message: String) { /* no-op */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Theme is applied via AndroidManifest to avoid double-splash effect
    }

    override fun onStart() {
        super.onStart()
        // Simple delay before navigating to login
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000) // 2 seconds delay
    }

}




