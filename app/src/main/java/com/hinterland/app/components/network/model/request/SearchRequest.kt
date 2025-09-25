package com.hinterland.app.components.network.model.request
import com.google.gson.annotations.SerializedName

data class SearchRequest(
    @SerializedName("skillId")
    val skillId: Int,

    @SerializedName("sortByRating")
    val sortByRating: Boolean,

    @SerializedName("latitude")
    val latitude: Double?,

    @SerializedName("longitude")
    val longitude: Double?,

    @SerializedName("sortByExperience")
    val sortByExperience: Boolean,

    @SerializedName("operatorLanguage")
    val operatorLanguage: String,

    @SerializedName("isVerified")
    val isVerified: Boolean,

    @SerializedName("town")
    val town: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("district")
    val district: String,

    @SerializedName("pinCode")
    val pinCode: String,

    @SerializedName("minRadius")
    val minRadius: Int,

    @SerializedName("maxRadius")
    val maxRadius: Int,

    @SerializedName("operatorName")
    val operatorName: String
)
