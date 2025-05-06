package com.example.colquegabriela

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAGviajero {
    private const val BASE_URL = "https://gabrielac01-agviajero.hf.space/"
    val aGviajeroApi: AGviajeroApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(AGviajeroApi::class.java)
    }
}