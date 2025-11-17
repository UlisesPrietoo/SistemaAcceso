package com.example.exampleapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores profesional (claro) para "Sistema Acceso"
private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = PureWhite,
    secondary = AccentBlue,
    onSecondary = PureWhite,
    background = PureWhite,
    onBackground = NavyBlue,
    surface = LightGray, // Fondo de tarjetas
    onSurface = NavyBlue,
    onSurfaceVariant = TextGray,
    error = ErrorRed,
    onError = PureWhite,
    outline = NavyBlue.copy(alpha = 0.5f) // Borde de OutlinedTextField
)

@Composable
fun SistemaAccesoTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Forzamos el tema claro profesional

    // Corrección: El SideEffect para statusBarColor obsoleto se ha eliminado.
    // MainActivity.kt con enableEdgeToEdge() y el Scaffold manejan esto.
    // Dejamos el SideEffect para el color de los iconos de la barra de estado.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Esto ajusta si los iconos de la barra de estado son claros u oscuros
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Esto ya no dará error
        shapes = Shapes, // Esto ya no dará error
        content = content
    )
}