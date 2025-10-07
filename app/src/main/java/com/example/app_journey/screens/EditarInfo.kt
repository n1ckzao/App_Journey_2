package com.example.app_journey.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.app_journey.model.Usuario
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.app_journey.service.RetrofitInstance
import com.example.app_journey.ui.theme.PurpleMedium
import retrofit2.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarInfo(
    navController: NavHostController,
    usuario: Usuario,
    onSave: (Usuario) -> Unit
) {
    var nome by remember { mutableStateOf(TextFieldValue(usuario.nome_completo)) }
    var email by remember { mutableStateOf(usuario.email) }

    val isModified = nome.text != usuario.nome_completo || email != usuario.email

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Informações") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PurpleMedium)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome completo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val usuarioAtualizado = usuario.copy(
                        nome_completo = nome.text,
                        email = email
                    )
                    onSave (usuarioAtualizado)
                    navController?.popBackStack()
                },
                enabled = isModified,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB8BDFA))
            ) {
                Text("Salvar", color = Color(0xFF341E9B))
            }
        }
    }
}
