package com.example.app_journey.model

data class GrupoResponse(
    val status: Boolean,
    val status_code: Int,
    val itens: Int,
    val grupos: List<Grupo>
)
