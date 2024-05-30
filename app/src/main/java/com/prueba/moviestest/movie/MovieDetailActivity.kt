package com.prueba.moviestest.movie

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.prueba.moviestest.R
import com.prueba.moviestest.api.ApiClient
import com.prueba.moviestest.api.MovieDetail
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var movieImage: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieDuration: TextView
    private lateinit var movieReleaseDate: TextView
    private lateinit var movieRating: TextView
    private lateinit var movieGenres: TextView
    private lateinit var movieDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        movieImage = findViewById(R.id.movieDetailImage)
        movieTitle = findViewById(R.id.movieDetailTitle)
        movieDuration = findViewById(R.id.movieDetailDuration)
        movieReleaseDate = findViewById(R.id.movieDetailReleaseDate)
        movieRating = findViewById(R.id.movieDetailRating)
        movieGenres = findViewById(R.id.movieDetailGenres)
        movieDescription = findViewById(R.id.movieDetailDescription)

        val movieId = intent.getIntExtra("movie_id", 0)
        loadMovieDetails(movieId)
    }

    private fun loadMovieDetails(movieId: Int) {
        val tmdbService = ApiClient.apiService

        tmdbService.getMovieDetails(movieId, "c0823934438075d63f1dbda4023e76fc")
            .enqueue(object : Callback<MovieDetail> {
                override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                    if (response.isSuccessful) {
                        response.body()?.let { movie ->
                            movieTitle.text = movie.title
                            movieDuration.text = "${movie.runtime} mins"
                            movieReleaseDate.text = movie.releaseDate
                            movieRating.text = movie.voteAverage.toString()
                            movieGenres.text = movie.genres.joinToString(", ") { it.name }
                            movieDescription.text = movie.overview
                            Picasso.get().load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                                .into(movieImage)
                        }
                    }
                }

                override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                    Toast.makeText(
                        this@MovieDetailActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
