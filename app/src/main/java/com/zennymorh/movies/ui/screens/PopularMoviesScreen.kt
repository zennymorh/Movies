package com.zennymorh.movies.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zennymorh.movies.R
import com.zennymorh.movies.data.PopularMoviesRepository
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.network.AppError
import com.zennymorh.movies.network.toAppError
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = "Trending",
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val refreshState = pagingData.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingScreen()
            }
            is LoadState.Error -> {
                val error = refreshState.error.toAppError()
                ErrorScreen(
                    error = error,
                    onRetry = { pagingData.retry() },
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(pagingData.itemCount) { index ->
            val movie = pagingData[index]
            movie?.let {
                MovieItem(movie = it, modifier = modifier)
            }
        }

        when (val appendState = pagingData.loadState.append) {
            is LoadState.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LoadingMoreIndicator()
                }
            }
            is LoadState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    AppendErrorItem(
                        error = appendState.error.toAppError(),
                        onRetry = { pagingData.retry() }
                    )
                }
            }
            else -> {}
        }
    }
}

@Composable
fun AppendErrorItem(error: AppError, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error loading more...",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red
        )
        Toast.makeText(LocalContext.current, error.toString(), Toast.LENGTH_SHORT).show()

        Button(onClick = onRetry) {
            Text(text = "Retry", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun LoadingScreen() {
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
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.fullPosterUrl)
                .crossfade(true)
                .placeholder(R.drawable.generic_movie)
                .build(),
            contentDescription = movie.title,
            modifier = Modifier
                .size(120.dp, 180.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
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
        Text(text = errorMessage, color = Color.Red, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
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
    override fun getMovies(): Flow<PagingData<PopularMovieEntity>> {
        val sampleMovies = List(10) { index ->
            PopularMovieEntity(
                id = index,
                title = "Movie Title ${index + 1}",
                overview = "This is a great movie overview.",
                posterPath = "/path.jpg",
                releaseDate = "2023-01-01",
            )
        }
        return flowOf(PagingData.from(sampleMovies))
    }
}
