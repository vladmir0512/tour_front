package com.bazaroff_alexey.newroutes

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("/api/users/register/") // Укажите правильный путь к вашему API
    fun sendRegister(@Body requestData: RequestData): Call<ResponseData>

    @POST("/api/users/login/") // Укажите правильный путь к вашему API
    fun sendLogin(@Body requestData: RequestData): Call<FirebaseAuthResponse>
}