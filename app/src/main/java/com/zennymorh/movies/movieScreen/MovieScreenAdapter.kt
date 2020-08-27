package com.zennymorh.movies.movieScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zennymorh.movies.Movie
import com.zennymorh.movies.R
import kotlinx.android.synthetic.main.movie_item.view.*

typealias ItemClickListener = (Movie) -> Unit
const val BASE_IMAGE_PATH = "https://image.tmdb.org/t/p/w780"

class MovieScreenAdapter(private var movieList: ArrayList<Movie>, var listener: ItemClickListener):
        RecyclerView.Adapter<MovieScreenAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = movieList[position]
        holder.bind(movie)
    }

    fun updateList(list: ArrayList<Movie>) {
        movieList = list
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.movie_item, parent, false)),
        View.OnClickListener {

        override fun onClick(v: View?) {
            val movie = movieList[adapterPosition]
            listener.invoke(movie)
        }

        var titleText: TextView? = null
        var dateText: TextView? = null
        var imageView: ImageView? = null

        init {
            titleText = itemView.findViewById(R.id.titleTV)
            dateText = itemView.findViewById(R.id.dateTV)
            imageView = itemView.findViewById(R.id.posterImgView)
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) {
            titleText?.text = movie.title
            dateText?.text = movie.releaseDate

            Picasso.get().load("$BASE_IMAGE_PATH${movie.posterPath}").into(itemView.posterImgView)
        }
    }
}