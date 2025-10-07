package com.example.app_journey.model

data class Grupo(
    val id_grupo: Int,
    val nome: String,
    val limite_membros: Int,
    val descricao: String,
    val imagem: String,
    val id_area: Int,
    val id_usuario: Int
)
