package com.hinterland.app.ui.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.hinterland.app.ui.base.BasePresenter
import kotlinx.coroutines.*
import com.hinterland.app.R
import com.hinterland.app.utils.SharedStateUtils

class LoginPresenter : BasePresenter<LoginView>() {

    private val interactor = LoginInteractor()
    private val job: Job = Job()
    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    fun onLoginButtonClicked(username: String, password: String, context: Context) {
        val currentView = view ?: return

        if (username.isEmpty() || password.isEmpty()) {
            currentView.showMessage("Invalid credentials")
            return
        }

        if (!isNetworkAvailable(context)) {
            currentView.showMessage("No internet connection")
            return
        }
        view?.showLoader()
        uiScope.launch {
            try {

                val parts = username.split("_")
                val userNameFinal = if (parts.size > 1) parts[1] else username
                val response = interactor.login(userNameFinal, password)
                view?.hideLoader()
                val token = response?.data?.token
                SharedStateUtils.token = token
                val statusMessage = response?.status?.statusMessage
                val statusCode = response?.status?.statusCode
                if (statusCode == 200) {
                    val profileResponse = interactor.getProfileDetails(token.toString())
                    if (profileResponse?.data != null) {
                        SharedStateUtils.userProfile = profileResponse.data
                    }
                }
                if (!token.isNullOrEmpty()) {
                    SharedStateUtils.username = userNameFinal
                    currentView.showMessage("Login success")
                    println("ProfileData:::${SharedStateUtils.userProfile}")
                    view?.landingScreen()
                } else {
                    currentView.showMessage(statusMessage ?: "Login failed")
                }
            } catch (e: Exception) {
                view?.hideLoader()
                Log.e("LoginPresenter", "login error", e)
                currentView.showMessage(view?.getString(R.string.invalid_username_or_password) ?: "")
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    override fun detachView() {
        super.detachView()
        job.cancel()
    }
}


