package com.example.app_journey.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefHelper {
    private const val PREF_NAME = "journey_prefs"
    private const val KEY_EMAIL = "email_usuario"
    private const val KEY_USER_ID = "id_usuario" // Nova constante para o ID

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun salvarEmail(context: Context, email: String) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_EMAIL, email)
            apply()
        }
    }
    fun getPrefs(context: Context): SharedPreferences {  // Removido 'private'
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun obterEmail(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_EMAIL, null)
    }
    fun recuperarEmail(context: Context): String? {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_EMAIL, null)
    }

    fun deslogar(context: Context) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove(KEY_EMAIL)
            apply()
        }
    }
    fun salvarIdUsuario(context: Context, id: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_USER_ID, id)
            .apply()
    }

    fun obterIdUsuario(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_USER_ID, -1) // -1 = não encontrado
    }
}