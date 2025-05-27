package com.example.rostro

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFoto {
    private const val BASE_URL = "https://gabrielac01-emociones.hf.space/" // URL de la API de Hugging Face
    val instance: FotoApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(FotoApi::class.java)
    }
}


