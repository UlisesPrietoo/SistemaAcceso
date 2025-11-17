package com.example.exampleapp.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Representa el estado del proceso de registro
// (Incluido aquí para no crear un archivo nuevo)
sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState.asStateFlow()

    // --- FUNCIÓN DE REGISTRO MEJORADA ---
    // Ahora incluye validaciones profesionales
    fun registerUser(name: String, email: String, pass: String, confirmPass: String) {
        // 1. Validaciones primero (evita llamadas innecesarias a Firebase)
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _registerState.value = RegisterState.Error("Todos los campos son obligatorios.")
            return
        }
        if (pass != confirmPass) {
            _registerState.value = RegisterState.Error("Las contraseñas no coinciden.")
            return
        }
        if (pass.length < 6) {
            _registerState.value = RegisterState.Error("La contraseña debe tener al menos 6 caracteres.")
            return
        }

        // 2. Iniciar el proceso de red
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                // 3. Crear usuario en Auth
                val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    // 4. VINCULACIÓN CON FIRESTORE (Requisito del profesor)
                    // Usamos 'name' (nombre completo) en lugar de 'username'
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "role" to "employee" // Rol por defecto
                    )
                    // Guardar en Firestore
                    firestore.collection("users").document(firebaseUser.uid).set(userMap).await()

                    _registerState.value = RegisterState.Success
                } else {
                    throw Exception("No se pudo crear el usuario.")
                }
            } catch (e: Exception) {
                // 5. Manejo de errores específico
                val errorMessage = when (e) {
                    is FirebaseAuthUserCollisionException -> "El correo ya está en uso."
                    else -> "Error de registro. Intente de nuevo."
                }
                _registerState.value = RegisterState.Error(errorMessage)
            }
        }
    }
}