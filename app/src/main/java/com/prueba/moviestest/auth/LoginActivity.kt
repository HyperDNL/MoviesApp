package com.prueba.moviestest.auth

import android.content.Intent
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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Comprobar si existe una sesión
        val currentUser = AuthController.getCurrentUser()
        if (currentUser != null) {
            val intent = Intent(this, MovieListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val login = findViewById<Button>(R.id.login)
        val usernameText = findViewById<EditText>(R.id.username)
        val passwordText = findViewById<EditText>(R.id.password)
        val signUpLink = findViewById<TextView>(R.id.sign_up_link)
        val progressBar = findViewById<ProgressBar>(R.id.login_progress_bar)

        // Lógica para el inicio de sesión
        login.setOnClickListener {
            val email = usernameText.text.toString()
            val password = passwordText.text.toString()

            login.isEnabled = false
            progressBar.visibility = ProgressBar.VISIBLE

            GlobalScope.launch(Dispatchers.Main) {
                val success = AuthController.loginUser(this@LoginActivity, email, password)

                progressBar.visibility = ProgressBar.GONE
                login.isEnabled = true

                if (success) {
                    NavigationUtil.redirectToDestination(
                        this@LoginActivity,
                        MovieListActivity::class.java
                    )
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error al iniciar sesión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Redirigir a la vista de registro
        signUpLink.setOnClickListener {
            NavigationUtil.redirectToDestination(this@LoginActivity, SignUpActivity::class.java)
        }

        // Habilita el botón de inicio de sesión si los campos username y password no están vacíos
        usernameText.addTextChangedListener {
            login.isEnabled = it?.isNotBlank() == true && passwordText.text.isNotBlank()
        }

        passwordText.addTextChangedListener {
            login.isEnabled = usernameText.text.isNotBlank() && it?.isNotBlank() == true
        }
    }
}