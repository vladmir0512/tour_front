package com.bazaroff_alexey.newroutes

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {
    /* FIREBASE AUTH and REGISTRATION */
    @POST("/api/users/register/") // Укажите правильный путь к вашему API
    fun sendRegister(@Body requestData: RequestData): Call<ResponseData>

    @POST("/api/users/login/") // Укажите правильный путь к вашему API
    fun sendLogin(@Body requestData: RequestData): Call<FirebaseAuthResponse>

    /* SEARCH COORDS (address to coords) */
    @GET("/api/search/")
    fun searchAddress(@Query("query") query: String): Call<AddressResponse>

    @GET("/api/users/avatar/")
    fun getUserAvatar(@Query("user_id") userId: String): Call<UserProfileResponse>

    @Multipart
    @POST("/api/users/upload-avatar/")
    fun uploadAvatar(
        @Part avatar: MultipartBody.Part,
        @Part("userId") userId: RequestBody
    ): Call<UploadAvatarResponse>

    @GET("/api/users/routes/")
    fun getRoutes(): Call<List<Route>>

    @POST("/api/users/routes/update-rating/")
    fun updateRouteRating(@Body ratingRequest: RatingRequest): Call<RatingResponse>

}
