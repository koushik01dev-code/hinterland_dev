package com.hinterland.app.components.network.model.response

data class JobListing(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val rating: String,
    val totalReviews: String,
    val city: String,
    val state: String,
    val profileImageUrl: String,
    val isAvailable: Boolean,
    val isVerified: Boolean = false,
    val language: String = "",
    val distance: Int? = null
)
