package com.example.nasadaily

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestModel {
    @GET("planetary/apod")
    fun setNasaApiKey(@Query("api_key") apiKey: String): Call<ResponseModel>
}