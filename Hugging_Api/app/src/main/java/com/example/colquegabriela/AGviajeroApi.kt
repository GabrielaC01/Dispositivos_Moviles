package com.example.colquegabriela

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
interface AGviajeroApi {
    @POST("/predict/")
    fun predict(@Body request: RequestData): Call<ResponseData>
}