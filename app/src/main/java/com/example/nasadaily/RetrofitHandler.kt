package com.example.nasadaily

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHandler {

    private fun makeRequest(): RequestModel {
        val podRetrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
        return podRetrofit.create(RequestModel::class.java)
    }

    internal fun sendServerRequest(key: String)
        = RequestAsyncTask().execute(this.makeRequest().setNasaApiKey(key)).get()
}