package com.zennymorh.movies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.zennymorh.movies.model.Movie
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zennymorh.movies.R

@Composable
fun PopularMoviesScreen(modifier: Modifier = Modifier) {

    val popularMoviesList = listOf(
        Movie(id = "one"),
        Movie(id = "two"),
        Movie(id = "three"),
        Movie(id = "four"),
    )

    LazyRow {
        items(popularMoviesList) {item ->

                Column(
                    Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = modifier
                            .size(64.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(50)),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                    Text(text = item.id!!)
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