package com.example.app_journey.model

data class Grupo(
    val id_grupo: Int = 0,
    val nome: String,
    val area: String,
    val limite_membros: Int,
    val descricao: String,
    val imagem: String
)
