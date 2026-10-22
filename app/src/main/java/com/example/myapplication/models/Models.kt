package com.example.severlapp_cenamad.models

data class LineaInvestigacion(
    val id: Int,
    val titulo: String,
    val descripcion: String
)

data class Investigador(
    val id: Int,
    val idLinea: Int,
    val nombre: String,
    val especialidad: String
)

data class Proyecto(
    val id: Int,
    val idInvestigador: Int,
    val titulo: String,
    val anio: Int,
    val estado: String
)

data class Publicacion(
    val id: Int,
    val idProyecto: Int,
    val titulo: String,
    val autor: String,
    val anio: Int,
    val requiereToken: Boolean = true
)