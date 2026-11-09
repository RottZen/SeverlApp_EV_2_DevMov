package com.example.myapplication.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.severlapp_cenamad.models.Investigador
import com.example.severlapp_cenamad.models.Proyecto
import com.example.severlapp_cenamad.models.Publicacion
import com.example.severlapp_cenamad.repository.Repository

private const val PREFS_NAME = "cenamad_prefs"
private const val KEY_TOKEN_VALIDADO = "token_validado"
private const val DEMO_E_TOKEN = "TOKEN2026"

internal fun isValidDemoEToken(code: String): Boolean = code.trim() == DEMO_E_TOKEN

data class DetalleProyectoUiState(
    val isLoaded: Boolean = false,
    val proyecto: Proyecto? = null,
    val investigador: Investigador? = null,
    val publicaciones: List<Publicacion> = emptyList(),
    val tokenValidado: Boolean = false,
    val mostrarModal: Boolean = false,
    val codigoIngresado: String = "",
    val errorValidacion: Boolean = false,
    val documentoEnDescarga: String = "",
    val mensaje: String? = null
)

class DetalleProyectoViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = application.getSharedPreferences(PREFS_NAME, Application.MODE_PRIVATE)

    var uiState by mutableStateOf(
        DetalleProyectoUiState(
            tokenValidado = preferences.getBoolean(KEY_TOKEN_VALIDADO, false)
        )
    )
        private set

    fun load(idProyecto: Int) {
        val proyecto = Repository.getProyectoById(idProyecto)
        uiState = uiState.copy(
            isLoaded = true,
            proyecto = proyecto,
            investigador = proyecto?.let { Repository.getInvestigadorById(it.idInvestigador) },
            publicaciones = Repository.getPublicacionesByProyecto(idProyecto)
        )
    }

    fun requestDownload(titulo: String, requiereToken: Boolean) {
        if (requiereToken && !uiState.tokenValidado) {
            uiState = uiState.copy(
                mostrarModal = true,
                codigoIngresado = "",
                errorValidacion = false,
                documentoEnDescarga = titulo,
                mensaje = null
            )
        } else {
            uiState = uiState.copy(mensaje = "Descarga simulada iniciada: $titulo")
        }
    }

    fun onCodigoChanged(code: String) {
        uiState = uiState.copy(codigoIngresado = code, errorValidacion = false)
    }

    fun validateToken() {
        if (!isValidDemoEToken(uiState.codigoIngresado)) {
            uiState = uiState.copy(errorValidacion = true)
            return
        }

        preferences.edit().putBoolean(KEY_TOKEN_VALIDADO, true).apply()
        uiState = uiState.copy(
            tokenValidado = true,
            mostrarModal = false,
            errorValidacion = false,
            mensaje = "¡E-Token validado con éxito! Descargando: ${uiState.documentoEnDescarga}"
        )
    }

    fun dismissTokenDialog() {
        uiState = uiState.copy(mostrarModal = false)
    }

    fun revokeToken() {
        preferences.edit().putBoolean(KEY_TOKEN_VALIDADO, false).apply()
        uiState = uiState.copy(tokenValidado = false, mensaje = "E-Token revocado para pruebas")
    }

    fun consumeMessage() {
        uiState = uiState.copy(mensaje = null)
    }
}
