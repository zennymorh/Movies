package com.zennymorh.movies.data

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.errorhandling.AppError
import com.zennymorh.movies.roomdb.PopularMovieDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.SocketTimeoutException

interface PopularMoviesRepository {
    fun getMovies(): Flow<Result<List<PopularMovieEntity>, AppError>>
    suspend fun fetchAndCacheMovies(): Result<List<PopularMovieEntity>, AppError>
}

class PopularMoviesRepositoryImpl @Inject constructor(
    private val movieDao: PopularMovieDao, private val apiService: ApiService,
): PopularMoviesRepository {

    override fun getMovies(): Flow<Result<List<PopularMovieEntity>, AppError>> = flow {
        emit(Ok(movieDao.getAllMovies().first()).takeIf { it.get()?.isNotEmpty() == true }
            ?: fetchAndCacheMovies())
    }.catch {
        emit(Err(AppError.UnknownError))
    }

    override suspend fun fetchAndCacheMovies(): Result<List<PopularMovieEntity>, AppError> {
        return try {
            val response = apiService.getPopularMovies()
            if (response.isSuccessful) {
                response.body()?.let { movieResponse ->
                    val movieEntities = movieResponse.results.map { movie ->
                        PopularMovieEntity(
                            id = movie.id,
                            title = movie.title,
                            overview = movie.overview,
                            posterPath = movie.posterPath,
                            releaseDate = movie.releaseDate
                        )
                    }
                    movieDao.insertMovies(movieEntities)
                    Ok(movieEntities)
                } ?: Err(AppError.EmptyResponseError)
            } else {
                Err(AppError.ServerError(response.code(), response.message()))
            }
        } catch (e: IOException) {
            Err(AppError.IOError)
        } catch (e: SocketTimeoutException) {
            Err(AppError.TimeoutError)
        } catch (e: Exception) {
            Err(AppError.UnknownError)
        }
    }
}