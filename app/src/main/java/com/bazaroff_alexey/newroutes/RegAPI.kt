package com.bazaroff_alexey.newroutes

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
    val expiresIn: String
)

data class AddressResponse(
    val lat: String,
    val lon: String,
    val address: String
)
//
//data class ImageData(
//    val userId: String, // ID пользователя
//    val imageUrl: String // URL фотографии
//)
