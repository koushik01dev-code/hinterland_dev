package com.hinterland.app.components.network.model.response

data class SkillListResponse(
    val respType: String,
    val metadata: Metadata,
    val status: Status,
    val data: List<Skill>
) {

    data class Metadata(
        val timestamp: String,
        val traceId: String
    )

    data class Status(
        val statusCode: Int,
        val statusMessage: String,
        val statusMessageKey: String
    )

    data class Skill(
        val id: Int,
        val name: String,
        val description: String,
        val skillImageUrl: String
    )
}