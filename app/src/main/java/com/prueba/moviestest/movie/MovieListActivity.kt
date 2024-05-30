package com.prueba.moviestest.movie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.prueba.moviestest.R
import com.prueba.moviestest.api.ApiClient
import com.prueba.moviestest.api.Movie
import com.prueba.moviestest.api.MovieResponse
import com.prueba.moviestest.auth.AuthController
import com.prueba.moviestest.auth.LoginActivity
import com.prueba.moviestest.utils.NavigationUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var welcomeTitle: TextView
    private lateinit var logoutButton: Button
    private val movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        recyclerView = findViewById(R.id.movielistRecyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        welcomeTitle = findViewById(R.id.title_welcome)
        logoutButton = findViewById(R.id.logout)

        val user = AuthController.getCurrentUser()

        Toast.makeText(this, "Bienvenidx ${user?.email}", Toast.LENGTH_LONG).show()

        welcomeTitle.text = "Bienvenidx ${user?.email}"

        recyclerView.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter(movies) { movie -> showMovieDetails(movie) }
        recyclerView.adapter = movieAdapter


        logoutButton.setOnClickListener {
            // Cerrar sesión utilizando el controlador
            AuthController.logoutUser()

            // Redirigir a la pantalla de inicio de sesión
            NavigationUtil.redirectToDestination(this@MovieListActivity, LoginActivity::class.java)
        }

        swipeRefreshLayout.setOnRefreshListener {
            loadMovies()
        }

        loadMovies()
    }

    private fun loadMovies() {
        swipeRefreshLayout.isRefreshing = true

        val tmdbService = ApiClient.apiService
        tmdbService.getNowPlayingMovies("c0823934438075d63f1dbda4023e76fc", 1)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    swipeRefreshLayout.isRefreshing = false

                    if (response.isSuccessful) {
                        response.body()?.results?.let {
                            movies.addAll(it)
                            movieAdapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false

                    Toast.makeText(this@MovieListActivity, "Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", movie.id)
        startActivity(intent)
    }
}
