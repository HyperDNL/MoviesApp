package com.prueba.moviestest.auth

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

object AuthController {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance();

    // Función asíncrona para registrar un usuario
    suspend fun signUpUser(context :Context, email: String,  password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Función asíncrona para iniciar sesión
    suspend fun loginUser(context: Context, email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }

    // Función para cerrar sesión
    fun logoutUser() {
        auth.signOut()
    }

    // Función para obtener el usuario actual
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}