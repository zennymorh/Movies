package com.zennymorh.movies.movieScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zennymorh.movies.Movie
import com.zennymorh.movies.R

typealias ItemClickListener = (Movie) -> Unit

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

        private var titleText: TextView? = null
        private var dateText: TextView? = null

        init {
            titleText = itemView.findViewById(R.id.titleTV)
            dateText = itemView.findViewById(R.id.dateTV)
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) {
            titleText?.text = movie.title
            dateText?.text = movie.releaseDate
        }
    }
}