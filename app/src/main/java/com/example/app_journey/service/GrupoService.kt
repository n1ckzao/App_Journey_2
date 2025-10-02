package com.example.app_journey.service

import com.example.app_journey.model.Grupo
import com.example.app_journey.model.GrupoResponse
import retrofit2.Call
import retrofit2.http.*

interface GrupoService {
    @Headers("Content-Type: application/json")
    @POST("group")
    fun inserirGrupo(@Body grupo: Grupo): Call<Grupo>

    @GET("group")
    fun listarGrupos(): Call<GrupoResponse>
}
