package com.example.exampleapp.ui.screens.login

// --- IMPORTACIONES EXPLÍCITAS PARA MATERIAL 3 ---
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
// --- FIN DE IMPORTACIONES ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val loginState by loginViewModel.loginState.collectAsState()
    var errorText by remember { mutableStateOf<String?>(null) }

    // Efecto para manejar la navegación y los errores
    LaunchedEffect(loginState) {
        loginViewModel.loginState.collectLatest { state ->
            when (state) {
                is LoginState.Success -> onLoginSuccess()
                is LoginState.Error -> errorText = state.message
                else -> errorText = null // Limpiar error en Idle o Loading
            }
        }
    }

    // Fondo degradado profesional
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.background
        ),
        startY = 0.0f,
        endY = 1500.0f // Ajusta este valor para controlar la transición
    )

    // Scaffold envuelve la pantalla, permitiendo barras superiores/inferiores
    Scaffold(
        // Usamos un Box para superponer el degradado en el fondo
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBrush)
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // --- Sección del Logo ---
                    Icon(
                        imageVector = Icons.Filled.Lock, // Un icono más profesional
                        contentDescription = "Logo de Seguridad",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Sistema Acceso",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary, // Texto blanco sobre el degradado
                    )
                    Text(
                        text = "Bienvenido. Inicie sesión.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // --- Tarjeta del Formulario ---
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            // --- Campo de Correo ---
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Correo Electrónico") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                // Icono (Soluciona tu error 'No parameter with name leadingIcon')
                                leadingIcon = {
                                    Icon(Icons.Default.Email, "Correo")
                                },
                                colors = OutlinedTextFieldDefaults.colors()
                            )

                            Spacer(Modifier.height(16.dp))

                            // --- Campo de Contraseña ---
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Contraseña") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                // Ocultar/mostrar contraseña
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                // Icono de candado
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, "Contraseña")
                                },
                                // Icono para mostrar/ocultar
                                trailingIcon = {
                                    val image = if (passwordVisible)
                                        Icons.Filled.LockOpen
                                    else
                                        Icons.Filled.Lock

                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(imageVector = image, "Mostrar/Ocultar contraseña")
                                    }
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // --- Sección de Error ---
                    errorText?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    // --- Botón de Login y Carga ---
                    if (loginState is LoginState.Loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    } else {
                        Button(
                            onClick = { loginViewModel.loginUser(email, password) },
                            enabled = email.isNotBlank() && password.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Iniciar Sesión", style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // --- Botón de Registro ---
                    TextButton(onClick = onGoToRegister) {
                        Text(
                            text = "¿No tienes cuenta? Regístrate aquí",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    )
}