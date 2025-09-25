package com.example.app_journey.model

data class Usuario(
    val id_usuario: Int = 0,
    val nome_completo: String = "",
    val email: String = "",
    val data_nascimento: String = "",
    val foto_perfil: String = "",
    val descricao: String = "",
    val senha: String = "",
    val tipo_usuario: String = ""
)
data class SenhaRequest(
    val senha: String = ""
)