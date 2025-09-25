package com.example.app_journey.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(navegacao: NavHostController?) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
    ){

    }
}

@Preview
@Composable
private fun HomePreview() {
    val fakeNav = rememberNavController()
    Home(navegacao = fakeNav)
}