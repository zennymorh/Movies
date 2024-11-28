package com.zennymorh.movies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zennymorh.movies.errorhandling.Result
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.zennymorh.movies.R
import com.zennymorh.movies.data.model.Movie
import com.zennymorh.movies.ui.viewmodel.PopularMoviesViewModel

@Composable
fun PopularMoviesScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: PopularMoviesViewModel = hiltViewModel()
) {
    // Collect the state from the ViewModel
    val moviesState by viewModel.moviesState.collectAsState()

    // When the screen is first created, trigger the movie fetch
    LaunchedEffect(Unit) {
        viewModel.fetchPopularMovies()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Add some padding around the content
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Popular Movies",
            fontStyle = FontStyle.Normal,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Popular Shows",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Handle different states
        when (moviesState) {
            is Result.Loading -> {
                // Show a loading indicator
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
            is Result.Success -> {
                val movies = (moviesState as Result.Success<List<Movie>>).data
                // Show the movie list
                HorizontalList(movies, modifier)
            }
            else -> {
                // TODO Add a failure screen
            }
        }
    }

}

@Composable
private fun HorizontalList(
    popularMoviesList: List<Movie>,
    modifier: Modifier
) {
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
                Text(text = item.title ?: "NO name found")
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