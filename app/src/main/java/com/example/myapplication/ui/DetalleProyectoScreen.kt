package com.example.myapplication.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.severlapp_cenamad.models.Publicacion
import com.example.severlapp_cenamad.repository.Repository

private const val PREFS_NAME = "cenamad_prefs"
private const val KEY_TOKEN_VALIDADO = "token_validado"

@Composable
fun DetalleProyectoScreen(
    idProyecto: Int,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Persistencia Local (SharedPreferences)
    var tokenValidado by remember {
        mutableStateOf(prefs.getBoolean(KEY_TOKEN_VALIDADO, false))
    }

    // Estado para controlar el despliegue del AlertDialog
    var mostrarModal by remember { mutableStateOf(false) }
    var codigoIngresado by remember { mutableStateOf("") }
    var errorValidacion by remember { mutableStateOf(false) }
    var documentoEnDescarga by remember { mutableStateOf("") }

    val proyecto = Repository.getProyectoById(idProyecto)
    val investigador = proyecto?.let { Repository.getInvestigadorById(it.idInvestigador) }
    val publicaciones = Repository.getPublicacionesByProyecto(idProyecto)

    // Lógica de inicio de descarga simulada
    val ejecutarDescarga: (String, Boolean) -> Unit = { tituloDoc, requiereToken ->
        documentoEnDescarga = tituloDoc
        if (requiereToken && !tokenValidado) {
            // Condiciona la apertura del modal a la ausencia del valor persistido
            codigoIngresado = ""
            errorValidacion = false
            mostrarModal = true
        } else {
            // Ya validado previamente en SharedPreferences o no requiere token
            Toast.makeText(
                context,
                "Descarga simulada iniciada: $tituloDoc",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Botón de navegación hacia atrás
        TextButton(
            onClick = onBackClick,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(text = "← Volver", fontSize = 16.sp)
        }

        if (proyecto == null) {
            Text(
                text = "Proyecto no encontrado",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            return@Column
        }

        // Encabezado del Proyecto
        Text(
            text = "Detalle del Proyecto",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = proyecto.titulo,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ficha del Proyecto e Investigador
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Año: ${proyecto.anio}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                    SuggestionChip(
                        onClick = {},
                        label = { Text(proyecto.estado, fontSize = 12.sp) }
                    )
                }

                if (investigador != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Investigador a cargo:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = investigador.nombre,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = investigador.especialidad,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Estado del E-Token y Persistencia
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (tokenValidado) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.tertiaryContainer
                }
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Estado E-Token:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = if (tokenValidado) {
                            "Autorizado en memoria persistente"
                        } else {
                            "No validado (solicitará modal)"
                        },
                        fontSize = 12.sp
                    )
                }

                if (tokenValidado) {
                    OutlinedButton(
                        onClick = {
                            prefs.edit().putBoolean(KEY_TOKEN_VALIDADO, false).apply()
                            tokenValidado = false
                            Toast.makeText(context, "E-Token revocado para pruebas", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("Reiniciar", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Listado de Documentos y Publicaciones
        Text(
            text = "Publicaciones y Descargas",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (publicaciones.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Ficha Técnica Completa (${proyecto.titulo})",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Documento oficial del proyecto CENAMAD. Requiere autorización E-Token.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { ejecutarDescarga("Ficha Técnica ${proyecto.titulo}", true) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Descargar Ficha Técnica")
                            }
                        }
                    }
                }
            } else {
                items(publicaciones) { pub ->
                    PublicacionItem(
                        publicacion = pub,
                        onDescargarClick = {
                            ejecutarDescarga(pub.titulo, pub.requiereToken)
                        }
                    )
                }
            }
        }
    }

    // Modal AlertDialog para validación de E-Token (Tarjeta 7)
    if (mostrarModal) {
        AlertDialog(
            onDismissRequest = { mostrarModal = false },
            title = {
                Text(
                    text = "Validación E-Token Requerida",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "El documento \"$documentoEnDescarga\" requiere validación de seguridad institucional.",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = codigoIngresado,
                        onValueChange = {
                            codigoIngresado = it
                            if (errorValidacion) errorValidacion = false
                        },
                        label = { Text("Código E-Token (ej: TOKEN2026)") },
                        singleLine = true,
                        isError = errorValidacion,
                        supportingText = if (errorValidacion) {
                            {
                                Text(
                                    text = "Código obligatorio. Ingrese un token válido.",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp
                                )
                            }
                        } else null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (codigoIngresado.trim().isNotEmpty()) {
                            // Persistencia en SharedPreferences tras validación exitosa
                            prefs.edit().putBoolean(KEY_TOKEN_VALIDADO, true).apply()
                            tokenValidado = true
                            mostrarModal = false
                            Toast.makeText(
                                context,
                                "¡E-Token validado con éxito! Descargando: $documentoEnDescarga",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            errorValidacion = true
                        }
                    }
                ) {
                    Text("Validar y Descargar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarModal = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun PublicacionItem(
    publicacion: Publicacion,
    onDescargarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = publicacion.titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Autor: ${publicacion.autor} • Año: ${publicacion.anio}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (publicacion.requiereToken) "Requiere E-Token" else "Acceso Libre",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (publicacion.requiereToken) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }
                )
                Button(
                    onClick = onDescargarClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Descargar", fontSize = 12.sp)
                }
            }
        }
    }
}
