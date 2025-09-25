package com.hinterland.app.utils

import com.hinterland.app.components.network.model.response.GetProfileResponse
import com.hinterland.app.components.network.model.response.SkillListResponse

object SharedStateUtils {
    var baseUrl: String = ""
    var latitude: Double? = null
    var longitude: Double? = null
    var town: String? = null
    var city: String? = null
    var district: String? = null
    var pinCode: String? = null
    var selectedLanguage: String? = null
    var username: String? = null
    var token: String? = null
    var allSkills: List<SkillListResponse.Skill> = emptyList()
    var userProfile: GetProfileResponse.UserData? = null
}


