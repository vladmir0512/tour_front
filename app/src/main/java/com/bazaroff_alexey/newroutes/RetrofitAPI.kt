package com.bazaroff_alexey.newroutes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPI {

   // private const val BASE_URL = "https://3e409f70-dcfa-434a-a665-1873873c30aa-00-27835ug5cnpfe.pike.replit.dev/" // Укажите адрес вашего локального сервера
    //private const val BASE_URL = "http://10.0.2.2:8000/" // Укажите адрес вашего локального сервера
    private const val BASE_URL = "http://89.104.66.155/" // Укажите адрес вашего локального сервера

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}