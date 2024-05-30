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

    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false

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

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreMovies()
                    }
                }
            }
        })

        logoutButton.setOnClickListener {
            // Cerrar sesión utilizando el controlador
            AuthController.logoutUser()

            // Redirigir a la pantalla de inicio de sesión
            NavigationUtil.redirectToDestination(this@MovieListActivity, LoginActivity::class.java)
        }

        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            isLastPage = false
            loadMovies(currentPage)
        }

        loadMovies(currentPage)
    }

    private fun loadMovies(page: Int) {
        isLoading = true
        swipeRefreshLayout.isRefreshing = true

        val tmdbService = ApiClient.apiService
        tmdbService.getNowPlayingMovies("c0823934438075d63f1dbda4023e76fc", page)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    swipeRefreshLayout.isRefreshing = false
                    isLoading = false

                    if (response.isSuccessful) {
                        response.body()?.results?.let {
                            if (page == 1) {
                                movies.clear()
                            }
                            movies.addAll(it)
                            movieAdapter.notifyDataSetChanged()
                            isLastPage =
                                it.isEmpty()
                        }
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                    isLoading = false
                    Toast.makeText(this@MovieListActivity, "Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun loadMoreMovies() {
        currentPage++
        loadMovies(currentPage)
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", movie.id)
        startActivity(intent)
    }
}
