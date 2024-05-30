package com.prueba.moviestest.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.prueba.moviestest.R
import com.prueba.moviestest.movie.MovieListActivity
import com.prueba.moviestest.utils.NavigationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        val signup = findViewById<Button>(R.id.sign_up)
        val usernameText = findViewById<EditText>(R.id.username_sign_up)
        val passwordText = findViewById<EditText>(R.id.password_sign_up)
        val signInLink = findViewById<TextView>(R.id.sign_in_link)
        val progressBar = findViewById<ProgressBar>(R.id.signup_progress_bar)

        // Lógica para el registro
        signup.setOnClickListener {
            val email = usernameText.text.toString()
            val password = passwordText.text.toString()

            signup.isEnabled = false
            progressBar.visibility = ProgressBar.VISIBLE

            GlobalScope.launch(Dispatchers.Main) {
                val success = AuthController.signUpUser(this@SignUpActivity, email, password)

                progressBar.visibility = ProgressBar.GONE
                signup.isEnabled = true

                if (success) {
                    NavigationUtil.redirectToDestination(this@SignUpActivity, MovieListActivity::class.java)
                } else {
                    Toast.makeText(this@SignUpActivity, "Error al registrarse", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Redirigir a la vista de inicio de sesión
        signInLink.setOnClickListener {
            NavigationUtil.redirectToDestination(this@SignUpActivity, LoginActivity::class.java)
        }

        // Habilita el botón de registrarse si los campos username y password no están vacíos
        usernameText.addTextChangedListener {
            signup.isEnabled = it?.isNotBlank() == true && passwordText.text.isNotBlank()
        }

        passwordText.addTextChangedListener {
            signup.isEnabled = usernameText.text.isNotBlank() && it?.isNotBlank() == true
        }
    }
}