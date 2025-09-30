package com.example.app_journey.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.107.134.28:8080/") // ajuste para sua API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val usuarioService: UsuarioService = retrofit.create(UsuarioService::class.java)
}