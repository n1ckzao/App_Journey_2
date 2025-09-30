package com.example.app_journey.service

import com.example.app_journey.model.Grupo
import com.example.app_journey.model.Result
import retrofit2.Call
import retrofit2.http.*

interface GrupoService {
    @Headers("Content-Type: application/json")
    @POST("grupo")
    fun inserirGrupo(@Body grupo: Grupo): Call<Grupo>

    @GET("grupo")
    fun listarGrupos(): Call<Result>
}