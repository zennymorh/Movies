package com.zennymorh.movies.data

import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.roomdb.PopularMovieDao
import io.mockk.mockk
import org.junit.Before

class PopularMoviesRepositoryImplTest {

    private lateinit var repository: PopularMoviesRepository
    private val mockDao = mockk<PopularMovieDao>(relaxed = true)
    private val mockApi = mockk<ApiService>()

    @Before
    fun setUp() {
        repository = PopularMoviesRepositoryImpl(mockDao, mockApi)
    }
}