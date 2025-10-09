package com.example.app_journey

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app_journey.model.UsuarioResult
import com.example.app_journey.screens.*
import com.example.app_journey.service.RetrofitInstance
import com.example.app_journey.utils.SharedPrefHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // 🔹 Verifica se há usuário salvo no SharedPreferences
    LaunchedEffect(Unit) {
        val idSalvo = SharedPrefHelper.recuperarIdUsuario(context)
        if (idSalvo != null) {
            RetrofitInstance.usuarioService.getUsuarioPorId(idSalvo)
                .enqueue(object : Callback<UsuarioResult> {
                    override fun onResponse(
                        call: Call<UsuarioResult>,
                        response: Response<UsuarioResult>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            if (result != null && !result.usuario.isNullOrEmpty()) {
                                val usuarioLogado = result.usuario[0]
                                Log.d("MainActivity", "Usuário logado: ${usuarioLogado.nome_completo}")
                            } else {
                                Log.e("MainActivity", "Usuário não encontrado")
                            }
                        } else {
                            Log.e("MainActivity", "Erro ao carregar usuário: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<UsuarioResult>, t: Throwable) {
                        Log.e("MainActivity", "Erro de rede: ${t.message}")
                    }
                })
        } else {
            Log.w("MainActivity", "id_usuario não encontrado no SharedPreferences")
        }
    }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val rotaAtual = navBackStackEntry.value?.destination?.route

    // 🔹 Define quais telas mostram a AppBar
    val rotasComBarra = listOf("profile", "home", "criargrupo", "editar_info/{idUsuario}")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(onOptionSelected = {
                scope.launch { drawerState.close() }
            })
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
                                        painter = painterResource(id = R.drawable.logoclaro),
                                        contentDescription = "Logo",
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
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate("profile") }) {
                                Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("login") { Login(navController) }
                composable("cadastro") { Cadastro(navController) }
                composable("recuperacao_senha") { RecuperacaoSenha(navController) }
                composable("home") { Home(navController) }
                composable("profile") { Perfil(navController) }
                composable("criargrupo") { CriarGrupo(navController) }

                // ✅ ROTA ADICIONADA — ESSA FALTAVA E CAUSAVA O CRASH
                composable("editar_info/{idUsuario}") { backStackEntry ->
                    val idUsuario = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull()
                    EditarInfoWrapper(navController, idUsuario)
                }

                composable("redefinir_senha/{idUsuario}") { backStackEntry ->
                    val idUsuario = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull()
                    idUsuario?.let { RedefinirSenha(navController, it) }
                }

                composable("verificar_email/{email}") { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("email")
                    email?.let { VerificarEmail(navController, it) }
                }
            }
        }
    }
}
