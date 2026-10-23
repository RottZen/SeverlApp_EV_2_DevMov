package com.example.myapplication.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.DetalleLineaScreen
import com.example.severlapp_cenamad.ui.HomeScreen
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Pantalla Principal (Home)
        composable(route = Screen.Home.route) {
            HomeScreen(
                onLineaClick = { idLinea ->
                    // Navega a detalle de línea pasando el ID (Tarjeta 6)[cite: 2, 4]
                    navController.navigate(Screen.DetalleLinea.createRoute(idLinea))
                },
                onProyectoClick = { idProyecto ->
                    // Opcional: Aquí puedes redirigir al detalle del proyecto más adelante
                }
            )
        }

        // Pantalla de Detalle de Línea (Tarjeta 5 y 6)
        composable(
            route = Screen.DetalleLinea.route,
            arguments = listOf(navArgument("idLinea") { type = NavType.IntType })
        ) { backStackEntry ->
            val idLinea = backStackEntry.arguments?.getInt("idLinea") ?: 0
            DetalleLineaScreen(
                idLinea = idLinea,
                onProyectoClick = { idProyecto ->
                    // Aquí prepararás la siguiente navegación hacia la tarjeta de E-Token
                }
            )
        }
    }
}


