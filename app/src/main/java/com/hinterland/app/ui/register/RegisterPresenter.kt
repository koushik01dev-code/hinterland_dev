package com.hinterland.app.ui.register

import android.content.Context
import android.util.Log
import com.hinterland.app.R
import com.hinterland.app.components.network.model.request.RegisterRequest
import com.hinterland.app.ui.base.BasePresenter
import com.hinterland.app.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterPresenter() : BasePresenter<RegisterView>() {

    private val interactor = RegisterInteractor()
    private val job: Job = Job()
    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    fun onRegisterClicked(
        username: String, email: String, password: String, confirmPassword: String,
        firstName: String, lastName: String, phoneNumber: String, gender: String, context: Context
    ) {
        val currentView = view ?: return
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() || firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank() || gender.isBlank()) {
            currentView.showMessage("Please fill all fields")
            return
        }

        if (!NetworkUtils.isNetworkAvailable(context)) {
            currentView.onRegisterError(context.getString(R.string.error_no_internet))
            return
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            currentView.showMessage("Please enter a valid email address")
            return
        }

        if (password != confirmPassword) {
            currentView.showMessage("Passwords do not match")
            return
        }

        if (phoneNumber.length != 10 || !phoneNumber.all { it.isDigit() }) {
            currentView.showMessage("Enter a valid 10-digit phone number")
            return
        }

        if (password.length < 8) {
            currentView.showMessage("Password must be at least 8 characters")
            return
        }

        view?.showLoader()
        uiScope.launch {
            try {
                val initial = username.trim().firstOrNull()?.uppercaseChar() ?: 'U'
                val profileUrl = "https://api.dicebear.com/7.x/initials/png?seed=$initial"
                val genderVal = gender.trim().uppercase()
                val body = RegisterRequest(
                    username = username,
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    profileImageUrl = profileUrl,
                    gender = genderVal
                )
                interactor.register(body)
                view?.hideLoader()
                currentView.onRegisterSuccess()
            } catch (e: Exception) {
                view?.hideLoader()
                Log.e("RegisterPresenter", "register error", e)
                val message = when (e) {
                    is HttpException -> when (e.code()) {
                        400 -> "Invalid input"
                        401, 403 -> "Not authorized"
                        409 -> "User already exists"
                        else -> "Registration failed"
                    }

                    else -> "Registration failed"
                }
                currentView.onRegisterError(message)
            }
        }
    }

    override fun detachView() {
        super.detachView()
        job.cancel()
    }

}


