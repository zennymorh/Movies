package com.zennymorh.movies.movieScreen


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zennymorh.movies.Movie
import com.zennymorh.movies.R

/**
 * A simple [Fragment] subclass.
 */
class MoviesScreenFragment : Fragment() {

    private val movieScreenAdapter: MovieScreenAdapter by lazy {
        MovieScreenAdapter(arrayListOf(), onMovieItemSelected)
    }

    private val onMovieItemSelected by lazy {
        object : ItemClickListener {
            override fun invoke(movie: Movie) {
                val action
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_screen, container, false)
    }


}
