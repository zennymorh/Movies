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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.zennymorh.movies.R
import com.zennymorh.movies.data.PopularMoviesRepository
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.errorhandling.AppError
import com.zennymorh.movies.ui.viewmodel.PopularMoviesViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun PopularMoviesScreen(
    modifier: Modifier = Modifier,
    viewModel: PopularMoviesViewModel = hiltViewModel()
) {
    val pagingData = viewModel.movies.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Trending Movies",
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
                ErrorScreen(
                    error = AppError.UnknownError,
                    onRetry = { viewModel.refreshPopularMovies() },
                    modifier = modifier
                )
            }
            else -> {
                PopularMovieList(modifier, pagingData)
            }
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun PopularMovieList(
    modifier: Modifier,
    pagingData: LazyPagingItems<PopularMovieEntity>
) {
    val three = 3
    LazyVerticalGrid(
        columns = GridCells.Fixed(three), // Define the number of columns, e.g., 2
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(three.dp),
        verticalArrangement = Arrangement.spacedBy(three.dp) // Add vertical spacing if needed
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
                item(span = { GridItemSpan(maxLineSpan) }) { // Span all columns
                    LoadingMoreIndicator()
                }
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@Suppress("MagicNumber")
fun MovieItem(movie: PopularMovieEntity, modifier: Modifier) {
    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier
                .size(120.dp, 180.dp)
                .clip(RoundedCornerShape(4)),
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
fun ErrorScreen(error: AppError, onRetry: () -> Unit, modifier: Modifier) {
    val errorMessage = when (error) {
        AppError.NetworkError -> stringResource(R.string.no_internet)
        AppError.TimeoutError -> stringResource(R.string.timeout_request)
        is AppError.ServerError -> stringResource(
            R.string.api_error,
            error.code,
            error.message ?: "Unknown Api Error"
        )
        AppError.UnknownError -> stringResource(R.string.generic_error)
        AppError.EmptyResponseError -> stringResource(R.string.empty_response_error)
        AppError.DatabaseError -> stringResource(R.string.database_error)
    }

    Column(
        modifier = modifier
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
fun PreviewPopularMovies() {
    PopularMoviesScreen(
        viewModel = PopularMoviesViewModel(FakePopularMoviesRepository())
    )
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun PreviewPopularMoviesError() {
    MaterialTheme {
        ErrorScreen(
            error = AppError.NetworkError,
            onRetry = {},
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun PreviewPopularMoviesLoading() {
    MaterialTheme {
        LoadingScreen()
    }
}

@Suppress("MagicNumber")
class FakePopularMoviesRepository : PopularMoviesRepository {
    override fun getMovies(): Flow<Result<PagingData<PopularMovieEntity>, AppError>> {
        val sampleMovies = List(10) { index ->
            PopularMovieEntity(
                id = index,
                title = "Movie Title ${index + 1}",
                overview = "This is a great movie overview for movie ${index + 1}.",
                posterPath = "/path${index + 1}.jpg",
                releaseDate = "2023-01-${index + 1}",
            )
        }
        return flowOf(Ok(PagingData.from(sampleMovies)))
    }
}
