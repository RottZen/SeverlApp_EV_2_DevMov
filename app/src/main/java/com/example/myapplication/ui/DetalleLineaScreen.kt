package com.example.myapplication.ui
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.severlapp_cenamad.models.Investigador
import com.example.severlapp_cenamad.models.Proyecto
import com.example.severlapp_cenamad.repository.Repository

@Composable
fun DetalleLineaScreen(
    idLinea: Int,
    onProyectoClick: (Int) -> Unit = {}
) {
    // Obtenemos los datos desde el repositorio estático
    val linea = Repository.getLineaById(idLinea)
    val investigadores = Repository.getInvestigadoresByLinea(idLinea)

    // Filtramos los proyectos que pertenecen a los investigadores de esta línea
    val proyectos = Repository.proyectos.filter { proyecto ->
        investigadores.any { it.id == proyecto.idInvestigador }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (linea != null) {
            Text(
                text = "Detalle en Línea:",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = linea.titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = linea.descripcion,
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Sección de Investigadores (LazyRow horizontal)[cite: 2]
        Text(
            text = "Investigadores",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(investigadores) { investigador ->
                InvestigadorAvatarItem(investigador = investigador)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Proyectos y publicaciones (LazyColumn vertical)[cite: 2]
        Text(
            text = "Proyectos y publicaciones",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(proyectos) { proyecto ->
                ProyectoDetalleItem(
                    proyecto = proyecto,
                    onClick = { onProyectoClick(proyecto.id) }
                )
            }
        }
    }
}

@Composable
fun InvestigadorAvatarItem(investigador: Investigador) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = investigador.nombre.take(2).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = investigador.nombre,
            fontSize = 11.sp,
            maxLines = 1,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProyectoDetalleItem(proyecto: Proyecto, onClick: () -> Unit) {
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
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Año: ${proyecto.anio} • Estado: ${proyecto.estado}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}