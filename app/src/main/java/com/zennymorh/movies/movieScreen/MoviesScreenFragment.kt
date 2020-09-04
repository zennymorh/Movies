package com.zennymorh.movies.movieScreen


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.zennymorh.movies.Movie
import com.zennymorh.movies.R
import com.zennymorh.movies.detailScreen.DetailScreenFragment
import kotlinx.android.synthetic.main.fragment_movies_screen.*

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
                val action = MoviesScreenFragmentDirections.actionMoviesScreenFragment2ToDetailScreenFragment(movie)
                findNavController().navigate(action)
            }
        }
    }

    private val viewModel: MovieScreenViewModel by lazy {
        ViewModelProviders.of(this).get(MovieScreenViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        
        return inflater.inflate(R.layout.fragment_movies_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movies.observe(viewLifecycleOwner, Observer { newList ->
            movieScreenAdapter.updateList(newList)
        })

        movieRecycler.adapter = movieScreenAdapter

        val manager = GridLayoutManager(activity, 2)
        movieRecycler.layoutManager = manager
    }


}
