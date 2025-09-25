package com.hinterland.app.components.network.model.response

data class Filters(
    val availableOnly: Boolean = false,
    val verifiedUsersOnly: Boolean = false,
    val language: Language? = null,
    val maxDistance: Int? = null
)

enum class Language(val displayName: String) {
    ENGLISH("English"),
    HINDI("Hindi"),
    KANNADA("Kannada");

    override fun toString(): String = displayName
}