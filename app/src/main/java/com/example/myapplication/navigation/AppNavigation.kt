
package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.DetalleLineaScreen
import com.example.myapplication.ui.DetalleProyectoScreen
import com.example.myapplication.ui.PantallaIndicadores
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
                    // Navega a detalle de línea pasando el ID (Tarjeta 6)
                    navController.navigate(Screen.DetalleLinea.createRoute(idLinea))
                },
                onProyectoClick = { idProyecto ->
                    // Navega a detalle del proyecto y validación de E-Token (Tarjeta 7)
                    navController.navigate(Screen.DetalleProyecto.createRoute(idProyecto))
                },
                onIndicadoresClick = {
                    // Navega a los gráficos de indicadores (Tarjeta 8)
                    navController.navigate(Screen.Indicadores.route)
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
                    // Navegación hacia la tarjeta de E-Token (Tarjeta 7)
                    navController.navigate(Screen.DetalleProyecto.createRoute(idProyecto))
                }
            )
        }

        // Pantalla de Detalle de Proyecto y Simulación E-Token (Tarjeta 7)
        composable(
            route = Screen.DetalleProyecto.route,
            arguments = listOf(navArgument("idProyecto") { type = NavType.IntType })
        ) { backStackEntry ->
            val idProyecto = backStackEntry.arguments?.getInt("idProyecto") ?: 0
            DetalleProyectoScreen(
                idProyecto = idProyecto,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Indicadores con gráficos (Tarjeta 8)
        composable(route = Screen.Indicadores.route) {
            PantallaIndicadores()
        }
    }
}
 