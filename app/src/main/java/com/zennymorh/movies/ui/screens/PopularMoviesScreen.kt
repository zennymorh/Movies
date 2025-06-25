package com.zennymorh.movies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.zennymorh.movies.R
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.errorhandling.AppError
import com.zennymorh.movies.ui.viewmodel.PopularMoviesViewModel

@Composable
fun PopularMoviesScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: PopularMoviesViewModel = hiltViewModel()
) {
    val pagingData = viewModel.movies.collectAsLazyPagingItems()

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

        when (pagingData.loadState.refresh) { // Check refresh state first
            is LoadState.Loading -> {
                LoadingScreen()
            }
            is LoadState.Error -> {
                ErrorScreen(error = AppError.UnknownError) { viewModel.refreshPopularMovies() }
            }
            else -> {
                PopularMovieList(modifier, pagingData)
            }
        }

        Text(
            text = "Popular Shows",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Composable
private fun PopularMovieList(
    modifier: Modifier,
    pagingData: LazyPagingItems<PopularMovieEntity>
) {
    LazyRow(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pagingData.itemCount) { index ->
            val movie = pagingData[index] // Access item using index
            movie?.let {
                MovieItem(movie = it, modifier = modifier)
            }
        }
        // Handle the load state for appending
        when (pagingData.loadState.append) {
            is LoadState.Loading -> {
                item { LoadingMoreIndicator() } // Show loading indicator at the end
            }

            is LoadState.Error -> {
                // Handle append error if needed
            }

            else -> {}
        }
    }
}

@Composable
fun LoadingScreen() {
    // Show loading indicator while data is being fetched
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadingMoreIndicator() {
    // Show a loading indicator for more data being appended
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MovieItem(movie: PopularMovieEntity, modifier: Modifier) {
    // A single movie item in the list
    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier
                .size(120.dp, 180.dp)
                .clip(RoundedCornerShape(8)),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.generic_movie), // Use your image loading logic here
            contentDescription = movie.title
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ErrorScreen(error: AppError, onRetry: () -> Unit) {
    val errorMessage = when (error) {
        AppError.NetworkError -> "No Internet Connection. Please check your network."
        AppError.TimeoutError -> "The request took too long. Try again."
        is AppError.ServerError -> "Server Error: ${error.code} - ${error.message ?: "Unknown"}"
        AppError.UnknownError -> "An unexpected error occurred."
        AppError.EmptyResponseError -> "No movies found. Try again later."
        AppError.DatabaseError -> "Failed to retrieve movies from database."
        AppError.IOError -> "IO Error."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage, color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPopularMovies(
) {
    PopularMoviesScreen()
}