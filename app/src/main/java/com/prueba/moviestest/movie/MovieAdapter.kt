package com.prueba.moviestest.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prueba.moviestest.R
import com.prueba.moviestest.api.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(
    private val movies: List<Movie>,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { onItemClick(movie) }
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.movieTitle)
        private val image: ImageView = itemView.findViewById(R.id.movieImage)
        private val rating: TextView = itemView.findViewById(R.id.movieRating)

        fun bind(movie: Movie) {
            title.text = movie.title
            rating.text = movie.voteAverage.toString()
            Picasso.get().load("https://image.tmdb.org/t/p/w500${movie.posterPath}").into(image)
        }
    }
}
