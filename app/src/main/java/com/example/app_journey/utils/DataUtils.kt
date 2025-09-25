package com.example.app_journey.utils


import java.text.SimpleDateFormat
import java.util.*
import kotlin.let
import kotlin.text.isNullOrBlank
import kotlin.text.startsWith

fun String.formatarData(): String {
    return try {
        val formatoOriginal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val formatoDestino = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val data = formatoOriginal.parse(this)
        data?.let { formatoDestino.format(it) } ?: this
    } catch (e: Exception) {
        this // Em caso de erro, retorna a string original
    }
}

fun isImagemValida(url: String?): Boolean {
    return !url.isNullOrBlank() && (url.startsWith("http://") || url.startsWith("https://"))
}