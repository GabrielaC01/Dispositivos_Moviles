package com.example.gato

import okhttp3.MultipartBody

// como vas a enviar los datos al HF
data class RequestData(val file: MultipartBody.Part)
// como vas a recibir los datos del HF
data class ResponseData(val prediction: String, val image: String)