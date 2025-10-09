package com.example.app_journey.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app_journey.model.Grupo
import com.example.app_journey.service.RetrofitFactory
import com.example.app_journey.utils.AzureUploader
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.app_journey.model.GruposResult

@Composable
fun CriarGrupo(navegacao: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var limite by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<Uri?>(null) }
    var imagemUrl by remember { mutableStateOf<String?>(null) }
    var mensagem by remember { mutableStateOf("") }
    var enviando by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imagemUri = uri
        uri?.let {
            scope.launch {
                enviando = true
                val inputStream = context.contentResolver.openInputStream(it)
                val fileName = "imagem_${System.currentTimeMillis()}.jpg"
                if (inputStream != null) {
                    val url = AzureUploader.uploadImageToAzure(inputStream, fileName)
                    if (url != null) {
                        imagemUrl = url
                        Toast.makeText(context, "Imagem enviada com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Falha no upload da imagem", Toast.LENGTH_SHORT).show()
                    }
                }
                enviando = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEBFF))
            .padding(20.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Crie seu Grupo no Journey!",
                color = Color(0xFF341E9B),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF341E9B), RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    // Botão Voltar
                    Button(
                        onClick = { navegacao.navigate("home") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDEBFF)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF341E9B))
                        Text("  Voltar", color = Color(0xFF341E9B), fontWeight = FontWeight.Bold)
                    }

                    // Campos de texto
                    CampoTexto("Nome do Grupo:", nome) { nome = it }
                    CampoTexto("Área específica:", area) { area = it }
                    CampoTexto("Limite de Membros (max):", limite.filter { c -> c.isDigit() }) { limite = it }
                    CampoTexto("Descrição do Grupo:", descricao) { descricao = it }

                    // Upload de imagem
                    Text(
                        "Imagem do Grupo:",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color(0xFF4A33C3), RoundedCornerShape(25.dp))
                            .clickable { launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imagemUrl != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imagemUrl),
                                contentDescription = "Imagem Selecionada",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                "📁 Selecionar Arquivo",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }

                    // Botão criar grupo
                    Button(
                        onClick = {
                            if (nome.isBlank() || area.isBlank() || limite.isBlank() || descricao.isBlank() || imagemUrl == null) {
                                mensagem = "Preencha todos os campos e selecione uma imagem"
                                return@Button
                            }

                            val grupoService = RetrofitFactory().getGrupoService()
                            val novoGrupo = Grupo(
                                id_grupo = 0,
                                nome = nome,
                                limite_membros = limite.toInt(),
                                descricao = descricao,
                                imagem = imagemUrl!!,
                                id_area = area,
                                id_usuario = 1
                            )

                            grupoService.inserirGrupo(novoGrupo).enqueue(object : Callback<GruposResult> {
                                override fun onResponse(
                                    call: Call<GruposResult>,
                                    response: Response<GruposResult>
                                ) {
                                    if (response.isSuccessful && response.body()?.status == true) {
                                        Toast.makeText(context, "Grupo criado com sucesso!", Toast.LENGTH_SHORT).show()
                                        navegacao.navigate("home")
                                    } else {
                                        mensagem = "Erro ao criar grupo: ${response.code()}"
                                    }
                                }

                                override fun onFailure(call: Call<com.example.app_journey.model.GruposResult>, t: Throwable) {
                                    mensagem = "Erro de rede: ${t.message}"
                                }
                            })
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDEBFF)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                    ) {
                        Text(
                            "➕ Criar Grupo",
                            color = Color(0xFF341E9B),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (mensagem.isNotEmpty()) {
                        Text(
                            mensagem,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }

    if (enviando) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@Composable
fun CampoTexto(label: String, valor: String, aoMudar: (String) -> Unit) {
    val outlinedColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.Gray,
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent
    )
    Column {
        Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        OutlinedTextField(
            value = valor,
            onValueChange = aoMudar,
            shape = RoundedCornerShape(33.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = outlinedColors
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCriarGrupoDefinitivo() {
    val fakeNav = rememberNavController()
    CriarGrupo(fakeNav)
}
