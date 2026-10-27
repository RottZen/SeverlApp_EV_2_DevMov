package com.example.myapplication.navigation
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DetalleLinea : Screen("detalle_linea/{idLinea}") {
        fun createRoute(idLinea: Int) = "detalle_linea/$idLinea"
    }
    object DetalleProyecto : Screen("detalle_proyecto/{idProyecto}") {
        fun createRoute(idProyecto: Int) = "detalle_proyecto/$idProyecto"
    }
}