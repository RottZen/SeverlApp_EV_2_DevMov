package com.example.severlapp_cenamad.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.severlapp_cenamad.models.LineaInvestigacion
import com.example.severlapp_cenamad.models.Proyecto
import com.example.severlapp_cenamad.repository.Repository

@Composable
fun HomeScreen(
    onLineaClick: (Int) -> Unit = {},
    onProyectoClick: (Int) -> Unit = {},
    onIndicadoresClick: () -> Unit = {}
) {
    var texto by remember { mutableStateOf("") }

    val proyectosFiltrados = remember(texto) {
        Repository.buscarProyectos(texto)
    }

    val lineasFiltradas = remember(texto) {
        if (texto.isBlank()) {
            Repository.lineas
        } else {
            Repository.lineas.filter {
                it.titulo.contains(texto, ignoreCase = true) ||
                        it.descripcion.contains(texto, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título a la izquierda y logo a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "CENAMAD Explorer",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Image(
                painter = painterResource(id = R.drawable.arbol),
                contentDescription = "Logo CENAMAD",
                modifier = Modifier.size(64.dp)
            )
        }

        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Buscar por título, año o palabra clave...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        // Tarjeta 8: acceso a los gráficos de indicadores
        Button(
            onClick = onIndicadoresClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Ver indicadores")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (lineasFiltradas.isNotEmpty()) {
                item {
                    Text(
                        text = "Líneas de Investigación",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(lineasFiltradas) { linea ->
                    LineaCard(linea = linea, onClick = { onLineaClick(linea.id) })
                }
            }

            item {
                Text(
                    text = if (texto.isBlank()) "Proyectos Destacados" else "Resultados de Proyectos (${proyectosFiltrados.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            }

            if (proyectosFiltrados.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron coincidencias para \"$texto\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(proyectosFiltrados) { proyecto ->
                    ProyectoCard(proyecto = proyecto, onClick = { onProyectoClick(proyecto.id) })
                }
            }
        }
    }
}

@Composable
fun LineaCard(
    linea: LineaInvestigacion,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = linea.titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = linea.descripcion,
                fontSize = 13.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProyectoCard(
    proyecto: Proyecto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = proyecto.titulo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Año: ${proyecto.anio}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = proyecto.estado,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}