package com.example.app_journey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app_journey.screens.Cadastro
import com.example.app_journey.screens.Home
import com.example.app_journey.screens.Login
import com.example.app_journey.screens.RecuperacaoSenha
import com.example.app_journey.screens.RedefinirSenha
import com.example.app_journey.screens.VerificarEmail
import com.example.app_journey.screens.DrawerMenu
import com.example.app_journey.screens.ProfileScreen
import com.example.app_journey.utils.SharedPrefHelper
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val rotaInicial = if (SharedPrefHelper.obterEmail(context) != null) {
        "login"
    } else {
        "login"
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Observa a rota atual
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val rotaAtual = navBackStackEntry.value?.destination?.route

    // Somente telas que devem mostrar a AppBar + Drawer
    val rotasComBarra = listOf(
        "profile",
        "home"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                onOptionSelected = {
                    scope.launch { drawerState.close() }
                }
            )
        },
        gesturesEnabled = drawerState.isOpen
    ) {
        Scaffold(
            topBar = {
                if (rotaAtual in rotasComBarra) {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            navController.navigate("home") {
                                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                            }
                                        }
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.logoclaro), // sua imagem no drawable
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Text(
                                    "Journey",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = {  navController.navigate("profile") }) {
                                Icon(Icons.Default.AccountCircle, contentDescription = "Modo de tema")
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = rotaInicial,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("login") { Login(navController) }
                composable("cadastro") { Cadastro(navController) }
                composable("recuperacao_senha") { RecuperacaoSenha(navController) }
                composable("home") { Home(navController) }
                composable("profile") { ProfileScreen(navController) }
                composable("verificar_email/{email}") { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("email")
                    if (email != null) {
                        VerificarEmail(navController, email)
                    }
                }
                composable("redefinir_senha/{idUsuario}") { backStackEntry ->
                    val idUsuario = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull()
                    if (idUsuario != null) {
                        RedefinirSenha(navController, idUsuario)
                    }
                }
            }
        }
    }
}