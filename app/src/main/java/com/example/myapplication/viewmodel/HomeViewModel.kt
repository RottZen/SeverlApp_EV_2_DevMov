package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.severlapp_cenamad.models.LineaInvestigacion
import com.example.severlapp_cenamad.models.Proyecto
import com.example.severlapp_cenamad.repository.Repository

data class HomeUiState(
    val query: String = "",
    val lineas: List<LineaInvestigacion> = Repository.lineas,
    val proyectos: List<Proyecto> = Repository.proyectos
)

class HomeViewModel : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    fun onQueryChanged(query: String) {
        val termino = query.trim()
        uiState = HomeUiState(
            query = query,
            lineas = if (termino.isEmpty()) {
                Repository.lineas
            } else {
                Repository.lineas.filter { linea ->
                    linea.titulo.contains(termino, ignoreCase = true) ||
                        linea.descripcion.contains(termino, ignoreCase = true)
                }
            },
            proyectos = Repository.buscarProyectos(query)
        )
    }
}
