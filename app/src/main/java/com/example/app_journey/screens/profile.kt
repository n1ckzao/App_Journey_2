package com.example.app_journey.screens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.app_journey.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para selecionar imagem
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleMedium,
                    titleContentColor = White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = PrimaryPurple)
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardInfoPessoais(
                profileImageUri = profileImageUri,
                onSelectImage = { launcher.launch("image/*") },
                onEditClick = { navController.navigate("editar_info") }
            )
            CardBio(onEditClick = { navController.navigate("editar_bio") })
        }
    }
}

@Composable
fun CardInfoPessoais(
    profileImageUri: Uri?,
    onSelectImage: () -> Unit,
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar
                if (profileImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        tint = White,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Button(
                    onClick = onSelectImage,
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleLighter)
                ) {
                    Text("Enviar foto", color = Color(0xFF341E9B))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            InfoRow("Nome completo", "Wilton Pereira Sampaio")
            InfoRow("Email", "xxxxxxxxxx@gmail.com")
            InfoRow("Telefone", "(11) 94321-2345")
        }
    }
}

@Composable
fun CardBio(onEditClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PurpleDarker),
        border = androidx.compose.foundation.BorderStroke(1.dp, PurpleMedium)
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
                "Sou desenvolvedor full-stack com experiência em desenvolvimento de sistemas, APIs, IoT, JS, Python e Kotlin. Apaixonado por tecnologia e inovação.",
                color = White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", color = White, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
        Text(text = value, color = White)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val fakeNav = rememberNavController()
    ProfileScreen(navController = fakeNav)
}