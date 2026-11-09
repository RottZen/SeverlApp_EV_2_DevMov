package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.severlapp_cenamad.models.Investigador
import com.example.severlapp_cenamad.models.LineaInvestigacion
import com.example.severlapp_cenamad.models.Proyecto
import com.example.severlapp_cenamad.repository.Repository

data class DetalleLineaUiState(
    val linea: LineaInvestigacion?,
    val investigadores: List<Investigador>,
    val proyectos: List<Proyecto>
)

class DetalleLineaViewModel : ViewModel() {
    fun load(idLinea: Int): DetalleLineaUiState {
        val investigadores = Repository.getInvestigadoresByLinea(idLinea)
        val investigadorIds = investigadores.mapTo(HashSet()) { it.id }
        return DetalleLineaUiState(
            linea = Repository.getLineaById(idLinea),
            investigadores = investigadores,
            proyectos = Repository.proyectos.filter { it.idInvestigador in investigadorIds }
        )
    }
}
