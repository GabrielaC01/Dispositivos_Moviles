package com.example.gato

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Multipart

interface FotoApi {
    @Multipart
    @POST("/predict/")
    fun predict(
        @Part file: MultipartBody.Part,
        @Query("objeto") objeto: String
    ): Call<ResponseData>
}