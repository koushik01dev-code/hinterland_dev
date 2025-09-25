package com.hinterland.app.components.network.model.response

data class GetProfileResponse(
    val metadata: Metadata?,
    val status: Status?,
    val messages: List<ApiMessage>?,
    val data: UserData?
) {

    data class Metadata(
        val timestamp: String?,
        val traceId: String?
    )

    data class Status(
        val statusCode: Int?,
        val statusMessage: String?,
        val statusMessageKey: String?
    )

    data class ApiMessage(
        val fieldName: String?,
        val messageType: String?,
        val errorType: String?,
        val messageKey: String?,
        val value: String?
    )

    data class UserData(
        val firstName: String?,
        val lastName: String?,
        val phoneNumber: String?,
        val email: String?,
        val profileImageUrl: String?,
        val id: Int?,
        val username: String?,
        val createdAt: String?,
        val updatedAt: String?,
        val gender: String?,
        val currentAddress: Address?
    )

    data class Address(
        val id: Int?,
        val line1: String?,
        val line2: String?,
        val city: String?,
        val state: String?,
        val country: String?,
        val postalCode: String?,
        val latitude: Double?,
        val longitude: Double?
    )
}

