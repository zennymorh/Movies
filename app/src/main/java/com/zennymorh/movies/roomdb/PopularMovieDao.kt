package com.zennymorh.movies.roomdb

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zennymorh.movies.data.model.PopularMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PopularMovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMoviesPaged(): PagingSource<Int, PopularMovieEntity>

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<PopularMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<PopularMovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearAll()
}
