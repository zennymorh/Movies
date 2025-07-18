package com.zennymorh.movies.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zennymorh.movies.data.model.PopularMovieEntity

@Database(entities = [PopularMovieEntity::class], version = 2, exportSchema = true)
abstract class PopularMovieDatabase : RoomDatabase() {
    abstract fun movieDao(): PopularMovieDao
}
