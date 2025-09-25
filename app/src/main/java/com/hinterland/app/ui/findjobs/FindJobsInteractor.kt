package com.hinterland.app.ui.findjobs

import com.hinterland.app.components.network.UserService
import com.hinterland.app.components.network.NetworkClient
import com.hinterland.app.components.network.model.request.SearchRequest
import com.hinterland.app.components.network.model.response.OperatorsResponse
import com.hinterland.app.components.network.model.response.SearchOperatorsResponse

class FindJobsInteractor {
    private val userService: UserService = NetworkClient.create(UserService::class.java)

    suspend fun searchOperator(request: SearchRequest): SearchOperatorsResponse? {
        return userService.searchOperator(request)
    }

    suspend fun getAllOperators(): OperatorsResponse {
        return userService.getAllOperators()
    }
}


