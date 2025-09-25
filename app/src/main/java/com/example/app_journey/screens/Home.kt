package com.example.app_journey.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(navegacao: NavHostController?) {
    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text(
                "Bem-vindo ao Journey!",
                fontSize = 37.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(9.dp))
            Text(
                fontSize = 17.sp,
                textAlign = TextAlign.Start,
                text = "Uma plataforma para mentoria e aprendizado colaborativo.",
                color = Color.Black
            )

            Card (

            ){}
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    val fakeNav = rememberNavController()
    Home(navegacao = fakeNav)
}