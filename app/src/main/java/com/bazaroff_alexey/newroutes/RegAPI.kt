package com.bazaroff_alexey.newroutes

import com.google.gson.annotations.SerializedName

data class RequestData(
    val email: String,
    val password: String
)

data class ResponseData(
    val message: String,
    val received_data: RequestData?
)

data class FirebaseAuthResponse(
    val message: String,
    val kind: String,
    val localId: String,
    val email: String,
    val displayName: String,
    val idToken: String,
    val registered: Boolean,
    val refreshToken: String,
    val expiresIn: String,
    val avatarUrl: String
)

data class AddressResponse(
    val lat: String,
    val lon: String,
    val address: String
)


data class UploadAvatarResponse(
    val avatarUrl: String
)

data class UserProfileResponse(
    @SerializedName("email") val email: String,
    @SerializedName("avatar_url") val avatarUrl: String?
)

data class Route(
    val id: Int,
    val name: String,
    val comment: String,
    val created_at: String,
    val user: Int,
    val distance: Double,
    val coords: String,
    val rating: Int
)

data class RatingRequest(
    val route_id: Int,
    val rating: Int
)

data class RatingResponse(
    val message: String
)
