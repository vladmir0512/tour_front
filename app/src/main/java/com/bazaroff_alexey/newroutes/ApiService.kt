package com.bazaroff_alexey.newroutes

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
//    @POST("/api/users/upload_image/")
//    fun uploadImage(@Body imageData: ImageData): Call<ResponseData> // Используйте подходящий тип ответа

}