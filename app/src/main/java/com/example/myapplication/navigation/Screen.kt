package com.example.myapplication.navigation
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DetalleLinea : Screen("detalle_linea/{idLinea}") {
        fun createRoute(idLinea: Int) = "detalle_linea/$idLinea"
    }
}