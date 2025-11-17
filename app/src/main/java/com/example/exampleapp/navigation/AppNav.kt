package com.example.exampleapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exampleapp.ui.screens.detail.DetailScreen
import com.example.exampleapp.ui.screens.home.HomeScreen
import com.example.exampleapp.ui.screens.login.LoginScreen
import com.example.exampleapp.ui.screens.register.RegisterScreen

@Composable
fun AppNav() {
    val nav = rememberNavController()
    // La pantalla de inicio es "login", lo cual es correcto.
    NavHost(navController = nav, startDestination = "login") {

        // --- Definición de la ruta "login" ---
        // Aquí es donde el usuario comienza.
        composable(route = "login") {
            // La llamada ya está correcta, con los dos callbacks de navegación
            LoginScreen(
                onLoginSuccess = { // Se ejecuta cuando el login es exitoso
                    // Navega a home y limpia el historial de navegación para que el usuario no pueda "volver atrás" al login
                    nav.navigate("home") {
                        popUpTo(nav.graph.startDestinationId) { inclusive = true }
                    }
                },
                onGoToRegister = { // Se ejecuta al pulsar "Regístrate"
                    nav.navigate("register")
                }
            )
        }

        // --- Definición de la ruta "home" ---
        // El usuario llega aquí después de un login exitoso.
        composable(route = "home") {
            // Llamamos a HomeScreen y le pasamos la acción para ir al detalle.
            HomeScreen(
                onGo = { id -> // 'id' es el identificador del item en HomeScreen
                    nav.navigate("detalle/$id")
                }
            )
        }

        // --- Definición de la ruta "detalle/{id}" ---
        // El usuario llega aquí desde HomeScreen.
        composable(route = "detalle/{id}") { backStackEntry ->
            // Se obtiene el 'id' de los argumentos de la ruta.
            val id = backStackEntry.arguments?.getString("id")
            // Es buena práctica manejar el caso en que el id sea nulo.
            requireNotNull(id) { "El ID no puede ser nulo" }
            DetailScreen(id = id)
        }

        // --- SECCIÓN CORREGIDA ---
        // Aquí estaba el error.
        // 'onGo' se cambió por 'onGoToLogin' para coincidir con RegisterScreen.kt
        composable(route = "register") {
            RegisterScreen(onGoToLogin = { // <-- CAMBIO HECHO AQUÍ

                nav.navigate("login") {

                    popUpTo("login") { inclusive = true}
                } })
        }
    }
}