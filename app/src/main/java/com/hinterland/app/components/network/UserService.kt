package com.hinterland.app.components.network

import com.hinterland.app.components.network.model.request.RegisterRequest
import com.hinterland.app.components.network.model.request.SearchRequest
import com.hinterland.app.components.network.model.response.AuthResponse
import com.hinterland.app.components.network.model.response.GetProfileResponse
import com.hinterland.app.components.network.model.response.OperatorsResponse
import com.hinterland.app.components.network.model.response.SearchOperatorsResponse
import com.hinterland.app.components.network.model.response.SkillListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST(Paths.LOGIN)
    suspend fun login(@Body body: Map<String, String>): AuthResponse?

    @POST(Paths.REGISTER)
    suspend fun register(@Body body: RegisterRequest): Unit

    @POST(Paths.SEARCH)
    suspend fun searchOperator(@Body body: SearchRequest): SearchOperatorsResponse?

    @GET(Paths.GET_NEARBY_OPERATORS)
    suspend fun getAllOperators(): OperatorsResponse

    @GET(Paths.GET_ALL_SKILLS)
    suspend fun getAllSkills(): SkillListResponse?

    @GET(Paths.PROFILE_DETAILS)
    suspend fun getProfileDetails(@Header("Authorization") token: String): GetProfileResponse?

}