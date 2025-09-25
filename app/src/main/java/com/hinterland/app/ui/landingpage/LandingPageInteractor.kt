package com.hinterland.app.ui.landingpage

import com.hinterland.app.components.network.NetworkClient
import com.hinterland.app.components.network.UserService
import com.hinterland.app.components.network.model.response.SkillListResponse

class LandingPageInteractor {
    private val userService: UserService = NetworkClient.create(UserService::class.java)

    suspend fun getAllSkills(): SkillListResponse? {
        return userService.getAllSkills()
    }
}