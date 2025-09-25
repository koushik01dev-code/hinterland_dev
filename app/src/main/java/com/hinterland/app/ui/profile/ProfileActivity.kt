package com.hinterland.app.ui.profile

import android.os.Bundle
import com.hinterland.app.databinding.ActivityProfileBinding
import com.hinterland.app.ui.base.BaseActivity

class ProfileActivity : BaseActivity<ProfilePresenter, ProfileView, ActivityProfileBinding>(), ProfileView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun provideViewBinding(): ActivityProfileBinding {
        return ActivityProfileBinding.inflate(layoutInflater)
    }

    override fun providePresenter(): ProfilePresenter {
        return ProfilePresenter()
    }

    override fun provideView(): ProfileView {
        return this
    }

    override fun showMessage(message: String) {

    }
}