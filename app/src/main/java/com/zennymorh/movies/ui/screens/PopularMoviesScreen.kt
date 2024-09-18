package com.zennymorh.movies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.zennymorh.movies.R
import com.zennymorh.movies.data.model.Movie
import com.zennymorh.movies.ui.viewmodel.PopularMoviesViewModel

@Composable
fun PopularMoviesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: PopularMoviesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.display()
    }

    val popularMoviesList = listOf(
        Movie(id = "one", title = "Anaconda"),
        Movie(id = "two", title = "The boss baby"),
        Movie(id = "three", title = "Shogun"),
        Movie(id = "four", title = "Godzilla"),
    )

    LazyRow {
        items(popularMoviesList) { item ->

            Column(
                Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = modifier
                        .size(120.dp, 300.dp)
                        .clip(RoundedCornerShape(8)),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.generic_movie),
                    contentDescription = null
                )
                Text(text = item.title!!)
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPopularMovies(
) {
    PopularMoviesScreen()
}