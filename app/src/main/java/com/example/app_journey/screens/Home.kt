package com.example.app_journey.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app_journey.model.Grupo
import com.example.app_journey.model.GrupoResponse
import com.example.app_journey.model.Area
import com.example.app_journey.model.AreaResponse
import com.example.app_journey.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Home(navegacao: NavHostController?) {
    val grupos = remember { mutableStateListOf<Grupo>() }
    val areas = remember { mutableStateListOf<Area>() }
    val context = LocalContext.current

    var pesquisa by remember { mutableStateOf("") }
    var areasSelecionadas by remember { mutableStateOf(setOf<Int>()) }

    // GET grupos e áreas
    LaunchedEffect(Unit) {
        RetrofitFactory().getGrupoService().listarGrupos().enqueue(object : Callback<GrupoResponse> {
            override fun onResponse(call: Call<GrupoResponse>, response: Response<GrupoResponse>) {
                if (response.isSuccessful) {
                    response.body()?.grupos?.let {
                        grupos.clear()
                        grupos.addAll(it)
                    }
                }
            }
            override fun onFailure(call: Call<GrupoResponse>, t: Throwable) {
                Toast.makeText(context, "Erro ao carregar grupos", Toast.LENGTH_SHORT).show()
            }
        })

        RetrofitFactory().getAreaService().listarAreas().enqueue(object : Callback<AreaResponse> {
            override fun onResponse(call: Call<AreaResponse>, response: Response<AreaResponse>) {
                if (response.isSuccessful) {
                    response.body()?.areas?.let {
                        areas.clear()
                        areas.addAll(it)
                    }
                }
            }
            override fun onFailure(call: Call<AreaResponse>, t: Throwable) {
                Toast.makeText(context, "Erro ao carregar áreas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Filtro dinâmico
    val gruposFiltrados = grupos.filter { grupo: Grupo ->
        val matchNome = grupo.nome.contains(pesquisa, ignoreCase = true)
        val matchArea = areasSelecionadas.isEmpty() || areasSelecionadas.contains(grupo.id_area)
        matchNome && matchArea
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔹 Card "Bem-vindo"
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4C3BCF))
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Text("Bem-vindo ao Journey!", fontSize = 26.sp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 🔹 Barra de pesquisa
        item {
            OutlinedTextField(
                value = pesquisa,
                onValueChange = { pesquisa = it },
                label = { Text("Pesquisar grupos...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 🔹 Filtros de área
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                areas.forEach { area: Area ->
                    val selecionado = areasSelecionadas.contains(area.id_area)
                    FilterChip(
                        selected = selecionado,
                        onClick = {
                            areasSelecionadas = if (selecionado) {
                                areasSelecionadas - area.id_area
                            } else {
                                areasSelecionadas + area.id_area
                            }
                        },
                        label = { Text(area.area) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF341E9B),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 🔹 Lista de grupos
        items(gruposFiltrados) { grupo: Grupo ->
            GrupoCard(grupo)
        }
    }
}
