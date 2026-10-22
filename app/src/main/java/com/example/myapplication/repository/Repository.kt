package com.example.severlapp_cenamad.repository

import com.example.severlapp_cenamad.models.LineaInvestigacion
import com.example.severlapp_cenamad.models.Investigador
import com.example.severlapp_cenamad.models.Proyecto
import com.example.severlapp_cenamad.models.Publicacion

object Repository {

    val lineas: List<LineaInvestigacion> = listOf(
        LineaInvestigacion(
            id = 1,
            titulo = "Bioproductos y Biorrefinería",
            descripcion = "Desarrollo de materiales sustentables, adhesivos verdes y compuestos de alto valor derivados de biomasa forestal."
        ),
        LineaInvestigacion(
            id = 2,
            titulo = "Edificación de Alto Rendimiento en Madera",
            descripcion = "Innovación en sistemas constructivos industrializados, Madera Contralaminada (CLT) y comportamiento estructural."
        ),
        LineaInvestigacion(
            id = 3,
            titulo = "Gestión e Inteligencia Forestal Sustentable",
            descripcion = "Monitoreo mediante sensores, modelamiento de crecimiento y optimización de la cadena de suministro forestal."
        )
    )

    val investigadores: List<Investigador> = listOf(
        Investigador(id = 101, idLinea = 1, nombre = "Dra. Sofía Soto", especialidad = "Química de Polímeros y Lignina"),
        Investigador(id = 102, idLinea = 1, nombre = "Dr. Marcelo Tapia", especialidad = "Biomateriales e Nano-celulosa"),
        Investigador(id = 103, idLinea = 2, nombre = "Ing. Carlos Mendoza", especialidad = "Ingeniería Estructural y Paneles CLT"),
        Investigador(id = 104, idLinea = 2, nombre = "Dra. Valentina Rojas", especialidad = "Eficiencia Energética y Hábitat"),
        Investigador(id = 105, idLinea = 3, nombre = "Dr. Esteban Silva", especialidad = "Geoinformática y Biomasa Forestal")
    )

    val proyectos: List<Proyecto> = listOf(
        Proyecto(id = 201, idInvestigador = 101, titulo = "Proyecto Alpha: Resinas Ecológicas libres de Formaldehído", anio = 2024, estado = "En Ejecución"),
        Proyecto(id = 202, idInvestigador = 102, titulo = "Aislamiento Térmico mediante Aerogeles de Celulosa", anio = 2023, estado = "Finalizado"),
        Proyecto(id = 203, idInvestigador = 103, titulo = "Sistema de Conexiones Sísmicas para Edificios de Madera en Altura", anio = 2024, estado = "En Ejecución"),
        Proyecto(id = 204, idInvestigador = 104, titulo = "Prototipo de Vivienda Social Sustentable de Huella Cero", anio = 2022, estado = "Finalizado"),
        Proyecto(id = 205, idInvestigador = 105, titulo = "Sistema EcoForest: Monitoreo Satelital de Captura de Carbono", anio = 2023, estado = "En Ejecución")
    )

    val publicaciones: List<Publicacion> = listOf(
        Publicacion(id = 301, idProyecto = 201, titulo = "Informe Técnico: Evaluación de Resinas a Base de Lignina", autor = "Dra. Sofía Soto", anio = 2024, requiereToken = true),
        Publicacion(id = 302, idProyecto = 203, titulo = "Manual de Diseño Estructural en Madera Contralaminada (CLT)", autor = "Ing. Carlos Mendoza", anio = 2024, requiereToken = true),
        Publicacion(id = 303, idProyecto = 205, titulo = "Guía Metodológica de Estimación de Biomasa con Datos LIDAR", autor = "Dr. Esteban Silva", anio = 2023, requiereToken = false)
    )

    fun getLineaById(id: Int): LineaInvestigacion? = lineas.find { it.id == id }
    fun getInvestigadoresByLinea(idLinea: Int): List<Investigador> = investigadores.filter { it.idLinea == idLinea }
    fun getInvestigadorById(id: Int): Investigador? = investigadores.find { it.id == id }
    fun getProyectosByInvestigador(idInvestigador: Int): List<Proyecto> = proyectos.filter { it.idInvestigador == idInvestigador }
    fun getProyectoById(id: Int): Proyecto? = proyectos.find { it.id == id }
    fun getPublicacionesByProyecto(idProyecto: Int): List<Publicacion> = publicaciones.filter { it.idProyecto == idProyecto }

    fun buscarProyectos(query: String): List<Proyecto> {
        if (query.isBlank()) return proyectos
        return proyectos.filter {
            it.titulo.contains(query, ignoreCase = true) || it.anio.toString().contains(query)
        }
    }
}