package com.zennymorh.movies.detailScreen

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.zennymorh.movies.Movie

import com.zennymorh.movies.R
import com.zennymorh.movies.movieScreen.BASE_IMAGE_PATH
import kotlinx.android.synthetic.main.detail_screen_fragment.*
import kotlinx.android.synthetic.main.movie_item.view.*

class DetailScreenFragment : Fragment() {

    private val args: DetailScreenFragmentArgs by navArgs()

    private lateinit var viewModel: DetailScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailScreenViewModel::class.java)

        val movie = args.selectedMovie

        expand_text_view.text = movie.overview
        Picasso.get().load("$BASE_IMAGE_PATH${movie.posterPath}").into(posterBG)
        movieTitleTV.text = movie.title
        releaseYearTV.text = "'${movie.releaseDate.substring(0,4)}"

    }

}
