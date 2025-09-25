package com.hinterland.app.ui.register

import com.hinterland.app.ui.base.BaseInteractor
import com.hinterland.app.components.network.NetworkClient
import com.hinterland.app.components.network.UserService
import com.hinterland.app.components.network.model.request.RegisterRequest

class RegisterInteractor : BaseInteractor {
    private val userService: UserService = NetworkClient.create(UserService::class.java)

    suspend fun register(body: RegisterRequest) {
        userService.register(body)
    }
}


