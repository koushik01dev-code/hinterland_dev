package com.hinterland.app.components.network.model.response

data class OperatorProfile(
    val id: Int,
    val bio: String,
    val isAvailable: Boolean,
    val rating: Double,
    val totalReviews: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val gender: String,
    val profileImageUrl: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)
