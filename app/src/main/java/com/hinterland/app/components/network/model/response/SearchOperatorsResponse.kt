package com.hinterland.app.components.network.model.response

data class SearchOperatorsResponse(
    val metadata: Metadata?,
    val status: Status?,
    val messages: List<ApiMessage>?,
    val data: SearchData?
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

    data class SearchData(
        val totalPages: Int?,
        val totalRecords: Int?,
        val operators: List<SearchOperator>?
    )

    data class SearchOperator(
        val id: Int?,
        val bio: String?,
        val isAvailable: Boolean?,
        val rating: Double?,
        val totalReviews: Int?,
        val user: User?,
        val createdAt: String?,
        val updatedAt: String?
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
