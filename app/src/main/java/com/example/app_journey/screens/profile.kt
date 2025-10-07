package com.example.app_journey.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app_journey.model.Usuario
import com.example.app_journey.model.UsuarioResult
import com.example.app_journey.service.RetrofitFactory
import com.example.app_journey.ui.theme.*
import com.example.app_journey.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun Perfil(navController: NavHostController) {
    val usuarioLogado = remember { mutableStateOf<Usuario?>(null) }
    val loading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val idUsuario = SharedPrefHelper.recuperarIdUsuario(context) ?: -1

    // Observa se o usuário foi atualizado na tela de edição
    val usuarioIdAtualizado =
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("usuarioIdAtualizado")

    // Função que busca o usuário pela API
    fun carregarUsuario(id: Int) {
        loading.value = true
        val usuarioService = RetrofitFactory().getUsuarioService()
        usuarioService.getUsuarioPorId(id)
            .enqueue(object : Callback<UsuarioResult> {
                override fun onResponse(
                    call: Call<UsuarioResult>,
                    response: Response<UsuarioResult>
                ) {
                    loading.value = false
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null && !result.usuario.isNullOrEmpty()) {
                            usuarioLogado.value = result.usuario[0]
                            errorMessage.value = null
                        } else {
                            errorMessage.value = "Usuário não encontrado"
                        }

                    } else {
                        errorMessage.value = "Erro ao carregar usuário: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<UsuarioResult>, t: Throwable) {
                    loading.value = false
                    errorMessage.value = "Erro de rede: ${t.message}"
                }
            })
    }

    // Carrega o usuário inicialmente
    LaunchedEffect(idUsuario) {
        if (idUsuario != -1) {
            carregarUsuario(idUsuario)
        } else {
            errorMessage.value = "Usuário não logado"
        }
    }

    // Recarrega o perfil automaticamente após edição
    LaunchedEffect(usuarioIdAtualizado) {
        usuarioIdAtualizado?.observeForever { id ->
            if (id != null) {
                carregarUsuario(id)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryPurple)
            .padding(16.dp)
    ) {
        when {
            loading.value -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            errorMessage.value != null -> Text(
                text = errorMessage.value ?: "Erro desconhecido",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )

            usuarioLogado.value != null -> {
                val usuario = usuarioLogado.value!!
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CardInfoPessoais(
                        profileImageUrl = usuario.foto_perfil,
                        nome = usuario.nome_completo,
                        email = usuario.email,
                        onEditClick = {
                            navController.navigate("editar_info/${usuario.id_usuario}")
                        }
                    )

                    CardBio(
                        descricao = usuario.descricao ?: "",
                        onEditClick = {
                            navController.navigate("editar_info/${usuario.id_usuario}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CardInfoPessoais(
    profileImageUrl: String?,
    nome: String?,
    email: String?,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PurpleDarker)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Informações pessoais",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleLighter)
                ) {
                    Text("Editar", color = Color(0xFF341E9B))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Foto de perfil
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!profileImageUrl.isNullOrBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUrl),
                        contentDescription = "Avatar do usuário",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar padrão",
                        tint = White,
                        modifier = Modifier.size(90.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            InfoRow("Nome completo", nome)
            InfoRow("Email", email)
        }
    }
}

@Composable
fun CardBio(onEditClick: () -> Unit, descricao: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PurpleDarker)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Biografia",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleLighter)
                ) {
                    Text("Editar", color = Color(0xFF341E9B))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = descricao.ifBlank { "Nenhuma biografia cadastrada" },
                color = White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", color = White, fontWeight = FontWeight.Medium)
        Text(text = value ?: "-", color = White)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPerfil() {
    val fakeNav = rememberNavController()
    Perfil(fakeNav)
}
