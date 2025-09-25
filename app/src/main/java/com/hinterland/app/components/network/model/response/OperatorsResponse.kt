package com.hinterland.app.components.network.model.response

data class OperatorsResponse(
    val data: OperatorsData?
) {
    data class OperatorsData(
        val totalPages: Int?,
        val totalRecords: Int?,
        val operators: List<Operator>?
    )

    data class Operator(
        val operatorSkills: List<OperatorSkill>?,
        val bio: String?,
        val isAvailable: Boolean?,
        val rating: Double?,
        val totalReviews: Int?,
        val id: Int?,
        val user: User?,
        val createdAt: String?,
        val updatedAt: String?
    )

    data class OperatorSkill(
        val skill: Skill?,
        val hourlyRate: Double?,
        val experienceYears: Int?
    )

    data class Skill(
        val id: Int?,
        val name: String?,
        val description: String?,
        val skillImageUrl: String?
    )

    data class User(
        val username: String?,
        val email: String?,
        val firstName: String?,
        val lastName: String?,
        val phoneNumber: String?,
        val gender: String?,
        val profileImageUrl: String?,
        val isActive: Boolean?,
        val currentAddress: CurrentAddress?
    )

    data class CurrentAddress(
        val id: Int?,
        val line1: String?,
        val line2: String?,
        val city: String?,
        val state: String?,
        val country: String?,
        val postalCode: String?,
        val latitude: Double?,
        val longitude: Double?,
        val createdAt: String?,
        val updatedAt: String?
    )
}
