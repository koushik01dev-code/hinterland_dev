package com.hinterland.app.ui.login

import com.hinterland.app.ui.base.BaseInteractor
import com.hinterland.app.components.network.NetworkClient
import com.hinterland.app.components.network.model.response.AuthResponse
import com.hinterland.app.components.network.UserService
import com.hinterland.app.components.network.model.response.GetProfileResponse

class LoginInteractor : BaseInteractor {

    private val userService: UserService = NetworkClient.create(UserService::class.java)

    suspend fun login(username: String, password: String): AuthResponse? {
        if (username.isEmpty() || password.isEmpty()) return null

        val params: Map<String, String> = mapOf(
            "username" to username,
            "password" to password
        )
        return userService.login(params)
    }

    suspend fun getProfileDetails(token: String): GetProfileResponse? {
        return userService.getProfileDetails("Bearer $token")
    }
}



