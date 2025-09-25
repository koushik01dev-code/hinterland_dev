package com.hinterland.app.components.network.model.response

data class AuthResponse(
    val respType: String = "",
    val metadata: Metadata,
    val status: Status?,
    val data: AuthData?
) {
    data class Metadata(
        val timestamp: String = "",
        val traceId: String = ""
    )

    data class Status(
        val statusCode: Int = 0,
        val statusMessage: String = "",
        val statusMessageKey: String = ""
    )

    data class AuthData(
        val token: String = "",
        val role: String = ""
    )
}